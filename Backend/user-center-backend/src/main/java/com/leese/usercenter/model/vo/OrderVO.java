package com.leese.usercenter.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderVO {
    // 基本資訊
    private String orderId;          // 訂單編號 (VARCHAR)
    private Long userId;             // 用户ID
    private Long addressId;          // 地址ID

    // 狀態資訊
    private Integer status;          // 訂單狀態 (數字代碼)
    private String statusLabel;      // 狀態中文標籤 (已接單、派送中等)
    private Integer payStatus;       // 支付狀態 (0未支付 1已支付 2已退款)
    private String payMethod;        // 支付方式

    // 金額資訊
    private Double totalAmount;      // 總金額
    private Integer packAmount;      // 打包費

    // 備註與原因
    private String remark;           // 備註
    private String cancelReason;     // 取消原因
    private String rejectionReason;  // 拒絕原因
    private String orderComment;     // 訂單備註

    // 時間資訊
    private String formattedTime;        // 格式化下單時間
    private String cancelTime;           // 取消時間
    private String estimatedDeliveryTime;// 預計送達時間
    private String deliveryTime;         // 實際送達時間
    private String createTime;           // 創建時間
    private String updateTime;           // 更新時間

    // 配送資訊
    private Integer deliveryStatus;  // 配送狀態 (1立即送出 0選擇時間)
    private String rideAddress;      // 地址快照
    private String riderName;        // 騎手姓名
    private String riderPhone;       // 騎手電話 ✅ 新增
    private String riderLocation;    // 騎手位置 ✅ 新增

    // 顧客資訊
    private String customerName;     // 顧客姓名 (可從 user 表 join 出來)

    // 菜品明細
    private List<OrderItemVO> items; // 訂單菜品列表
}
