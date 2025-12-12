package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName order_items
 */
@TableName(value ="order_items")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class OrderItems {
    private Integer id;

    private Integer orderId;

    private Integer dishId;

    private String dishName;

    private String dishFlavor;

    private Integer quantity;

    private BigDecimal price;
}