package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.mapper.*;
import com.leese.usercenter.model.dto.PlaceOrderDTO;
import com.leese.usercenter.model.entity.*;
import com.leese.usercenter.model.vo.OrderItemVO;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leese.usercenter.mod4.Order.AbstractOrderCommand;
import com.leese.usercenter.mod4.Order.OrderCommandFactory;
import com.leese.usercenter.Invoker.OrderInvoker;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RiderMapper riderMapper;
    private final CartMapper cartMapper;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderCommandFactory orderCommandFactory;

    @Autowired
    private OrderInvoker orderInvoker;
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            RiderMapper riderMapper,
                            CartMapper cartMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.riderMapper = riderMapper;
        this.cartMapper = cartMapper;
    }


    @Override
    @Transactional
    public boolean updatePaymentStatus(Long orderId, Integer payStatus, Integer status){
        int rows = orderMapper.updatePayStatus(orderId, payStatus, status);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderEntity createOrderFromCart(PlaceOrderDTO dto, Integer userId) {
        QueryWrapper<Cart> cartQuery = new QueryWrapper<>();
        cartQuery.in("id",dto.getCartIds())
                .eq("userId",userId);
        List<Cart> cartItems = cartMapper.selectList(cartQuery);

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("購物車為空，無法下單");
        }
        double totalAmount = cartItems.stream()
                .mapToDouble(cart -> cart.getAmount().doubleValue() * cart.getNumber())
                .sum();

        String orderNo = "ORD"+System.currentTimeMillis()+ "-" + UUID.randomUUID().toString().substring(0, 6);
        log.info("地址id:{}",dto.getAddressId());
        OrderEntity order = OrderEntity.builder()
                .orderId(orderNo)
                .userId(userId)
                .addressId(dto.getAddressId())      // 使用前端選中的地址
                .status(2)            // 默認狀態：待接單
                .totalAmount(totalAmount)
                .payMethod("線上支付")
                .payStatus(0)         // 待發貨
                .deliveryStatus(1)    // 立即送出
                .packAmount(0)
                .isDelete(0)
                .build();

        orderMapper.insert(order);
        Long orderPrimaryId = order.getId();
        log.info("✅ 生成訂單成功，orderId = {}, dbId = {}", order.getOrderId(), orderPrimaryId);

        System.out.println(order.getId());
        for(Cart cart : cartItems){
            Dish dish = dishMapper.selectById(cart.getDishId());
            orderItemMapper.insert(
                    OrderItemEntity.builder()
                            .orderId(orderPrimaryId)
                            .dishId(cart.getDishId().longValue())
                            .dishName(dish.getDishName())
                            .quantity(cart.getNumber())
                            .price(dish.getPrice().doubleValue())
                            .build()
            );
        }

        System.out.println("=== 開始刪除購物車 ===");
        System.out.println("用戶ID: " + userId);
        System.out.println("購物車IDs: " + dto.getCartIds());

//        QueryWrapper<Cart> deleteWrapper = new QueryWrapper<>();
//        deleteWrapper.in("id", dto.getCartIds())
//                .eq("userId", userId);  // 這裡也要用數據庫字段名！
//        cartMapper.delete(deleteWrapper);
        cartMapper.delete(new QueryWrapper<Cart>()
                .in("id",dto.getCartIds())
                .eq("userId",userId));
        return order;
    }

    @Override
    public OrderVO getOrderDetails(Long orderId) {
        OrderEntity order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("訂單不存在: " + orderId);
        }

        List<OrderItemEntity> items = orderItemMapper.findByOrderId(orderId);
        //RiderEntity rider = riderMapper.findById(order.getRiderId());
        return convertToVO(order, items, null);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Integer newStatus) {
        log.info("🔄 使用命令模式更新订单状态，orderId={}, newStatus={}", orderId, newStatus);

        // 验证状态范围
        if (newStatus < 1 || newStatus > 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "无效的状态码");
        }

        // 获取当前订单状态
        OrderEntity order = findById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "订单不存在");
        }

        Integer currentStatus = order.getStatus();
        log.info("📊 当前订单状态: {} -> 目标状态: {}", currentStatus, newStatus);

        // 如果状态相同，直接返回（无需转换）
        if (currentStatus.equals(newStatus)) {
            log.info("ℹ️ 订单状态未变化，无需更新");
            return;
        }

        try {
            // 获取对应的命令
            AbstractOrderCommand command = orderCommandFactory.getCommand(newStatus);

            // 设置命令参数
            command.setOrderId(orderId);
            command.setCurrentStatus(currentStatus);
            command.setNewStatus(newStatus);

            // 执行命令
            command.execute();

            log.info("✅ 订单状态更新成功: {} -> {}", getStatusText(currentStatus), getStatusText(newStatus));

        } catch (IllegalArgumentException e) {
            log.error("❌ 状态转换失败: {}", e.getMessage());
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("❌ 更新订单状态系统异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常，请稍后重试");
        }
    }


    private OrderVO convertToVO(OrderEntity order, List<OrderItemEntity> items, RiderEntity rider) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());          // ✅ 主鍵 id
        vo.setOrderId(order.getOrderId()); // 訂單編號（字串）
        vo.setUserId(order.getUserId());
        vo.setAddressId(Long.valueOf(order.getAddressId()));

        // 狀態
        vo.setStatus(order.getStatus());
        vo.setStatusLabel(mapStatus(order.getStatus()));
        vo.setPayStatus(order.getPayStatus());
        vo.setPayMethod(order.getPayMethod());

        // 金額
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPackAmount(order.getPackAmount());

        // 時間欄位格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo.setFormattedTime(order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : null);
        vo.setCancelTime(order.getCancelTime() != null ? sdf.format(order.getCancelTime()) : null);
        vo.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime() != null ? sdf.format(order.getEstimatedDeliveryTime()) : null);
        vo.setDeliveryTime(order.getDeliveryTime() != null ? sdf.format(order.getDeliveryTime()) : null);
        vo.setCreateTime(order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : null);
        vo.setUpdateTime(order.getUpdateTime() != null ? sdf.format(order.getUpdateTime()) : null);

        // 顧客資訊
        vo.setCustomerName("顧客姓名");

        // 菜品明細
        List<OrderItemVO> itemVOs = new ArrayList<>();
        for (OrderItemEntity item : items) {
            OrderItemVO itemVO = new OrderItemVO();
            itemVO.setId(item.getId());
            itemVO.setOrderId(item.getOrderId());
            itemVO.setDishId(item.getDishId());
            itemVO.setDishName(item.getDishName());
            itemVO.setDishFlavor(item.getDishFlavor());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setPrice(item.getPrice());

            BigDecimal subtotal = BigDecimal.valueOf(item.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            itemVO.setSubtotal(subtotal.doubleValue());

            itemVOs.add(itemVO);
        }
        vo.setItems(itemVOs);

        return vo;
    }

    @Override
    public OrderEntity findById(Long id) {
        return orderMapper.findById(id);
    }

    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1: return "待付款";
            case 2: return "待接单";
            case 3: return "已接单";
            case 4: return "制作中";
            case 5: return "派送中";
            case 6: return "已完成";
            case 7: return "已取消";
            case 8: return "退款";
            default: return "未知";
        }
    }

    /**
     * 映射状态为标签 - 原有的 mapStatus 方法
     */
    private String mapStatus(Integer status) {
        if (status == null) {
            return "未知狀態";
        }
        switch (status) {
            case 1: return "待付款";
            case 2: return "待接單";
            case 3: return "已接單";
            case 4: return "制作中";
            case 5: return "派送中";
            case 6: return "已完成";
            case 7: return "已取消";
            case 8: return "退款";
            default: return "待處理";
        }
    }


    @Override

    public List<OrderEntity> findByUserId(Integer userId) {
        log.info("🔍 Service 層接收到的 userId = {}", userId);
        List<OrderEntity> orders = orderMapper.findByUserId(userId);
        log.info("🔍 查詢結果 = {}", orders);
        return orders;
    }


    @Override
    public List<OrderEntity> findAll() {
        return orderMapper.findAll();
    }


}
