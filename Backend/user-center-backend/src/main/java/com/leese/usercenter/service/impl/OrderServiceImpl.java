package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.mapper.CartMapper;
import com.leese.usercenter.mapper.OrderItemMapper;
import com.leese.usercenter.mapper.OrderMapper;
import com.leese.usercenter.mapper.RiderMapper;
import com.leese.usercenter.model.entity.Cart;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.model.entity.OrderItemEntity;
import com.leese.usercenter.model.entity.RiderEntity;
import com.leese.usercenter.model.vo.OrderItemVO;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RiderMapper riderMapper;
    private final CartMapper cartMapper;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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
    public void updateOrderStatus(Long orderId, Integer status) {
        orderMapper.updateStatus(orderId, status);
    }

    /**
     * 從購物車生成訂單，必須指定收貨地址
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderEntity createOrderFromCart(Integer userId, Long addressId) {
        // 1. 查詢當前用戶購物車
        List<Cart> cartItems = cartMapper.selectList(
                new QueryWrapper<Cart>().eq("userId", userId)
        );
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("購物車為空，無法下單");
        }

        // 2. 計算總金額
        double totalAmount = cartItems.stream()
                .map(cart -> cart.getAmount() == null ? BigDecimal.ZERO : cart.getAmount())
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        // 3. 構建訂單主表數據
        String orderNo = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);

        OrderEntity order = OrderEntity.builder()
                .orderId(orderNo)
                .userId(userId)
                .addressId(addressId)      // 使用前端選中的地址
                .status(2)            // 默認狀態：待接單
                .totalAmount(totalAmount)
                .payMethod("線上支付")
                .payStatus(0)         // 未支付
                .deliveryStatus(1)    // 立即送出
                .packAmount(0)
                .isDelete(0)
                .build();

        // 4. 保存訂單主表，並獲取自增主鍵 id
        orderMapper.save(order);
        Long orderPrimaryId = order.getId();
        log.info("✅ 生成訂單成功，orderId = {}, dbId = {}", order.getOrderId(), orderPrimaryId);

        // 5. 生成訂單項
        for (Cart cart : cartItems) {
            if (cart.getNumber() == null || cart.getNumber() <= 0) {
                continue;
            }
            BigDecimal amount = cart.getAmount() == null ? BigDecimal.ZERO : cart.getAmount();
            BigDecimal quantity = BigDecimal.valueOf(cart.getNumber());
            BigDecimal unitPrice = quantity.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : amount.divide(quantity, 2, BigDecimal.ROUND_HALF_UP);

            OrderItemEntity item = OrderItemEntity.builder()
                    .orderId(orderPrimaryId)
                    .dishId(cart.getDishId() == null ? null : cart.getDishId().longValue())
                    .dishName(cart.getName())
                    .dishFlavor(cart.getDishFlavor())
                    .quantity(cart.getNumber())
                    .price(unitPrice.doubleValue())
                    .build();

            orderItemMapper.insert(item);
        }

        // 6. 清空該用戶購物車
        cartMapper.delete(new QueryWrapper<Cart>().eq("userId", userId));

        return order;
    }

    private OrderVO convertToVO(OrderEntity order, List<OrderItemEntity> items, RiderEntity rider) {
        OrderVO vo = new OrderVO();
        vo.setOrderId(order.getOrderId());
        vo.setUserId(order.getUserId());
        vo.setAddressId(order.getAddressId());

        // 狀態
        vo.setStatus(order.getStatus());
        vo.setStatusLabel(mapStatus(order.getStatus()));
        vo.setPayStatus(order.getPayStatus());
        vo.setPayMethod(order.getPayMethod());

        // 金額
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPackAmount(order.getPackAmount());

//        // 備註與原因
//        vo.setRemark(order.getRemark());
//        vo.setCancelReason(order.getCancelReason());
//        vo.setRejectionReason(order.getRejectionReason());
//        vo.setOrderComment(order.getOrderComment());

        // 時間欄位格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo.setFormattedTime(order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : null);
        vo.setCancelTime(order.getCancelTime() != null ? sdf.format(order.getCancelTime()) : null);
        vo.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime() != null ? sdf.format(order.getEstimatedDeliveryTime()) : null);
        vo.setDeliveryTime(order.getDeliveryTime() != null ? sdf.format(order.getDeliveryTime()) : null);
        vo.setCreateTime(order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : null);
        vo.setUpdateTime(order.getUpdateTime() != null ? sdf.format(order.getUpdateTime()) : null);

        // 配送資訊
//        vo.setDeliveryStatus(order.getDeliveryStatus());
//        vo.setRideAddress(order.getRideAddress());
//        vo.setRiderName(rider != null ? rider.getName() : null);
//        vo.setRiderPhone(rider != null ? rider.getPhone() : null);


        // 顧客資訊 (可從 user 表 join 出來，這裡先留空)
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

            // 小計用 BigDecimal 計算，避免浮點誤差
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

    private String mapStatus(Integer status) {
        if (status == null) {
            return "未知狀態";
        }
        switch (status) {
            case 1: return "待付款";
            case 2: return "待接單";
            case 3: return "已接單";
            case 4: return "派送中";
            case 5: return "已完成";
            case 6: return "已取消";
            case 7: return "退款";
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
