package com.leese.usercenter.service;

import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.model.entity.OrderEntity;
import java.util.List;

public interface OrderService {
    /**
     * 根據訂單 ID 獲取訂單詳情
     */
    OrderVO getOrderDetails(Long orderId);

    /**
     * 更新訂單狀態
     */
    void updateOrderStatus(Long orderId, Integer status);
    List<OrderEntity> findByUserId(Integer userId);
    List<OrderEntity> findAll();}
