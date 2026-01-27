package com.leese.usercenter.mod5.model.dto;

import java.math.BigDecimal;

/**
 * 支付请求
 */
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private Long userId;

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}