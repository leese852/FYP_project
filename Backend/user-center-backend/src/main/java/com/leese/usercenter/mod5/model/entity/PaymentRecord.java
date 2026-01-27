package com.leese.usercenter.mod5.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录
 */
@Data
@TableName(value = "payment_records")
public class PaymentRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "order_id")
    private Long orderId;

    @TableField(value = "user_id")
    private Long userId;

    private BigDecimal amount;

    @TableField(value = "payment_method")
    private String paymentMethod;

    private String status;

    @TableField(value = "transaction_id")
    private String transactionId;

    @TableField(value = "created_at")
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}