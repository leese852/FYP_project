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
    // 基本資訊 (對應 orders 表)
    private String orderId;          // 訂單編號
    private Integer userId;          // 用户ID
    private Long addressId;          // 地址ID

    // 狀態資訊 (對應 orders 表)
    private Integer status;          // 訂單狀態
    private Integer payStatus;       // 支付狀態
    private String payMethod;        // 支付方式

    // 金額資訊 (對應 orders 表)
    private Double totalAmount;      // 總金額
    private Integer packAmount;      // 打包費

    // 備註與原因 (對應 orders 表)
    private String remark;           // 備註
    private String cancelReason;     // 取消原因
    private String rejectionReason;  // 拒絕原因

    // 時間資訊 (對應 orders 表)
    private String cancelTime;           // 取消時間
    private String estimatedDeliveryTime;// 預計送達時間
    private String deliveryTime;         // 實際送達時間
    private String createTime;           // 創建時間
    private String updateTime;           // 更新時間

    // 配送資訊 (對應 orders 表)
    private Integer deliveryStatus;  // 配送狀態
    private Long riderId;            // 騎手ID

    // ================== 以下是前端展示用，DB 沒有的欄位 ==================
    @TableField(exist = false)
    private String statusLabel;      // 狀態中文標籤

    @TableField(exist = false)
    private String orderComment;     // 訂單備註 (前端展示)

    @TableField(exist = false)
    private String formattedTime;    // 格式化下單時間

    @TableField(exist = false)
    private String rideAddress;      // 地址快照

    @TableField(exist = false)
    private String riderName;        // 騎手姓名

    @TableField(exist = false)
    private String riderPhone;       // 騎手電話

    @TableField(exist = false)
    private String riderLocation;    // 騎手位置

    @TableField(exist = false)
    private String customerName;     // 顧客姓名

    @TableField(exist = false)
    private List<OrderItemVO> items; // 訂單菜品列表
}
