package com.leese.usercenter.service.impl;

import com.leese.usercenter.mapper.OrderItemMapper;
import com.leese.usercenter.mapper.OrderMapper;
import com.leese.usercenter.mapper.RiderMapper;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.model.entity.OrderItemEntity;
import com.leese.usercenter.model.entity.RiderEntity;
import com.leese.usercenter.model.vo.OrderItemVO;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RiderMapper riderMapper;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, RiderMapper riderMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.riderMapper = riderMapper;
    }

    @Override
    public OrderVO getOrderDetails(Long orderId) {
        OrderEntity order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("訂單不存在: " + orderId);
        }

        List<OrderItemEntity> items = orderItemMapper.findByOrderId(orderId);
        RiderEntity rider = riderMapper.findById(order.getRiderId());
        return convertToVO(order, items, rider);
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer status) {
        orderMapper.updateStatus(orderId, status);
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
