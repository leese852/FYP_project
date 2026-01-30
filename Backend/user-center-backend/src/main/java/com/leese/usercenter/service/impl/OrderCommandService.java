package com.leese.usercenter.service.impl;

import com.leese.usercenter.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCommandService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 只更新订单状态，不包含其他业务逻辑
     */
    public void updateOrderStatusOnly(Long orderId, Integer status) {
        log.info("📊 更新订单状态到数据库: orderId={}, status={}", orderId, status);
        orderMapper.updateStatus(orderId, status);
    }

    /**
     * 获取订单状态
     */
    public Integer getOrderStatus(Long orderId) {
        // 假设 OrderMapper 有这个查询方法
        // 如果没有，需要先添加
        return orderMapper.findStatusById(orderId);
    }

    /**
     * 验证订单是否存在
     */
    public boolean existsOrder(Long orderId) {
        return orderMapper.existsById(orderId);
    }
}