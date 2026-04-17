package com.leese.usercenter.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderVO {
    private Long id;
    private String orderId;
    private Integer userId;
    private Long addressId;
    private Integer status;
    private Integer payStatus;
    private String payMethod;
    private Double totalAmount;
    private Integer packAmount;
    private String remark;
    private String cancelReason;
    private String rejectionReason;
    private String cancelTime;
    private String estimatedDeliveryTime;
    private String deliveryTime;
    private String createTime;
    private String updateTime;
    private Integer deliveryStatus;
    private Long riderId;

    // 🔥 添加坐标字段
    private Double restaurantLat;
    private Double restaurantLng;
    private Double customerLat;
    private Double customerLng;

    @TableField(exist = false)
    private String statusLabel;

    @TableField(exist = false)
    private String orderComment;

    @TableField(exist = false)
    private String formattedTime;

    @TableField(exist = false)
    private String rideAddress;

    @TableField(exist = false)
    private String riderName;

    @TableField(exist = false)
    private String riderPhone;

    @TableField(exist = false)
    private String riderLocation;

    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private List<OrderItemVO> items;
}