package com.leese.usercenter.mod5.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付响应
 */
@Data
public class PaymentResponse {
    /**
     * 支付记录ID
     */
    private Long paymentId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 交易ID
     */
    private String transactionId;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;
}