package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName order_items
 */
@TableName(value ="order_items")
@Data
public class OrderItems {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 訂單id
     */
    private Integer orderId;

    /**
     * 菜品id
     */
    private Integer dishId;

    /**
     * 菜品名稱快照
     */
    private String dishName;

    /**
     * 菜品數量
     */
    private Integer quantity;

    /**
     * 價錢快照
     */
    private BigDecimal price;
}