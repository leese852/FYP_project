package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName order_items
 */
@TableName(value ="order_items")
@Data
public class OrderItems {
    private Integer id;

    private Integer orderId;

    private Integer dishId;

    private String dishName;

    private String dishFlavor;

    private Integer quantity;

    private BigDecimal price;
}