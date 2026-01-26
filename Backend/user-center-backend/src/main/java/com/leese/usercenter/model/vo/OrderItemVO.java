package com.leese.usercenter.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemVO {
    private Long id;             // 訂單項目ID (主鍵)
    private Long orderId;        // 訂單ID
    private Long dishId;         // 菜品ID
    private String dishName;     // 菜品名稱快照
    private String dishFlavor;   // 菜品口味 (JSON格式)
    private Integer quantity;    // 菜品數量
    private Double price;        // 單價快照
    private Double subtotal;     // 小計 (price * quantity)
}
