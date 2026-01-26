package com.leese.usercenter.service;

import com.leese.usercenter.model.vo.OrderVO;

public interface OrderService {
    OrderVO getOrderDetails(Long orderId);
    void updateOrderStatus(Long orderId, String status);
}
