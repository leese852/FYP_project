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
    private Integer userId;               // 用户 id
    private Long addressId;            // 地址 id
    private Integer status;            // 状态 (TINYINT: 1待付款 2待接單 3已接單 4派送中 5已完成 6已取消 7退款)
    private Double totalAmount;        // 总价钱
    private String payMethod;          // 支付方法
    private Integer payStatus;         // 支付状态 (0未支付 1已支付 2已退款)
    private String remark;             // 备注
    private String cancelReason;       // 取消原因
    private Timestamp cancelTime;      // 取消时间
    private String rejectionReason;    // 拒绝原因
    private Timestamp estimatedDeliveryTime; // 预计送达时间
    private Integer deliveryStatus;    // 配送状态 (1立即送出 0选择具体时间)
    private Timestamp deliveryTime;    // 送达时间
    private Integer packAmount;        // 打包费
    private Timestamp createTime;      // 创建时间
    private Timestamp updateTime;      // 更新时间
    private Integer isDelete;          // 是否删除 (0未刪除 1刪除)

    // ✅ 新增欄位，替代 employeeId
    private Long riderId;              // 配送騎手ID，關聯 rider 表

    // 其他快照欄位
     // 订单备注
}
