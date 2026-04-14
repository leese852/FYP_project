package com.leese.usercenter.service;

import com.leese.usercenter.model.dto.PlaceOrderDTO;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.model.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    /**
     * 根據訂單 ID 獲取訂單詳情
     */
    OrderVO getOrderDetails(Long orderId);
    OrderEntity findById(Long id);
    /**
     * 更新訂單狀態
     */
    void updateOrderStatus(Long orderId, Integer status);

    /**
     * 根據用戶 ID 查詢該用戶所有訂單
     */
    List<OrderEntity> findByUserId(Integer userId);

    /**
     * 查詢所有訂單（測試用）
     */
    List<OrderEntity> findAll();

    /**
     * 從當前用戶購物車生成訂單，必須指定收貨地址
     */
    OrderEntity createOrderFromCart(PlaceOrderDTO dto, Integer userId);

    boolean updatePaymentStatus(Long orderId, Integer payStatus, Integer status);
}
