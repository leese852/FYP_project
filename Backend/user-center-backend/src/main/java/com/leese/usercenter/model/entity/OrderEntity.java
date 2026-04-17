package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@TableName("orders")
public class OrderEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderId;
    private Integer userId;
    private Integer addressId;
    private Integer status;
    private Double totalAmount;
    private String payMethod;
    private Integer payStatus;
    private String remark;
    private String cancelReason;
    private Timestamp cancelTime;
    private String rejectionReason;
    private Timestamp estimatedDeliveryTime;
    private Integer deliveryStatus;
    private Timestamp deliveryTime;
    private Integer packAmount;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer isDelete;
    private Long riderId;

    // 🔥 添加 @TableField 注解映射数据库字段
    @TableField("restaurant_lat")
    private Double restaurantLat;

    @TableField("restaurant_lng")
    private Double restaurantLng;

    @TableField("customer_lat")
    private Double customerLat;

    @TableField("customer_lng")
    private Double customerLng;

    private Double riderEarning;
    private Double earningMultiplier;
    private Timestamp actualDeliveryTime;
    private Integer estimatedDeliveryDuration;
    private Integer preparationStatus;
    private Timestamp pickupTime;
}