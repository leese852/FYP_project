package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName orders
 */
@TableName(value ="orders")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class Orders {
    private Integer id;

    private String orderId;

    private Integer userId;

    private Integer addressId;

    private Integer status;

    private BigDecimal totalAmount;

    private String payMethod;

    private Integer payStatus;

    private String remark;

    private String cancelReason;

    private Date cancelTime;

    private String rejectionReason;

    private Date estimatedDeliveryTime;

    private Integer deliveryStatus;

    private Date deliveryTime;

    private Integer packAmount;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;
}