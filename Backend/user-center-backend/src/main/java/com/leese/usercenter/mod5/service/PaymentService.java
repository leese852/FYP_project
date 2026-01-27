package com.leese.usercenter.mod5.service;

import com.leese.usercenter.mod5.model.dto.PaymentRequest;
import com.leese.usercenter.mod5.model.dto.PaymentResponse;
import com.leese.usercenter.mod5.model.entity.PaymentRecord;

import java.util.List;

/**
 * 支付服务
 */
public interface PaymentService {

    /**
     * 处理支付
     */
    PaymentResponse processPayment(PaymentRequest paymentRequest);

    /**
     * 根据订单ID查询支付记录
     */
    PaymentRecord getPaymentByOrderId(Long orderId);

    /**
     * 根据用户ID查询支付记录
     */
    List<PaymentRecord> getPaymentsByUserId(Long userId);

    /**
     * 模拟支付回调
     */
    Boolean simulatePaymentCallback(Long orderId, String status);
}