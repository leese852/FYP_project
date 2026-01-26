package com.leese.usercenter.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemDTO {
    private Long id;          // 对应表的主键 id
    private Long dishId;      // 菜品 id
    private Integer quantity; // 数量
    private Double price;     // 单价
}
