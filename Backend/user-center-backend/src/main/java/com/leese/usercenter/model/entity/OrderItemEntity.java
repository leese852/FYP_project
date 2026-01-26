package com.leese.usercenter.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class OrderItemEntity {
    private Long id;            // 对应表的主键 id
    private Long orderId;       // 对应表的 orderId
    private Long dishId;        // 对应表的 dishId
    private String dishName;    // 对应表的 dishName
    private String dishFlavor;  // 对应表的 dishFlavor
    private Integer quantity;   // 对应表的 quantity
    private Double price;       // 对应表的 price
}
