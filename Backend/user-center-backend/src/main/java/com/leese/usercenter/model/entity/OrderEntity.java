package com.leese.usercenter.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
public class OrderEntity {
    private Long id;                   // 表主键 id
    private String orderId;            // 訂單編號 (VARCHAR)
    private Long userId;               // 用户 id
    private Long addressId;            // 地址 id
    private Integer status;            // 状态 (TINYINT)
    private Double totalAmount;        // 总价钱
    private String payMethod;          // 支付方法
    private Integer payStatus;         // 支付状态
    private String remark;             // 备注
    private String cancelReason;       // 取消原因
    private Timestamp cancelTime;      // 取消时间
    private String rejectionReason;    // 拒绝原因
    private Timestamp estimatedDeliveryTime; // 预计送达时间
    private Integer deliveryStatus;    // 配送状态
    private Timestamp deliveryTime;    // 送达时间
    private Integer packAmount;        // 打包费
    private Timestamp createTime;      // 创建时间
    private Timestamp updateTime;      // 更新时间
    private Integer isDelete;          // 是否删除
    private Long employeeId;           // 派送骑手 (如果有)
    private String rideAddress;        // 地址快照
    private String orderComment;       // 订单备注
}
