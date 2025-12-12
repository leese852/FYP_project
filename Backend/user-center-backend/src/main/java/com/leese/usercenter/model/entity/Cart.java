package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName cart
 */
@TableName(value ="cart")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class Cart {
    private Integer id;

    private String name;

    private Integer userId;

    private Integer dishId;

    private String dishFlavor;

    private Integer number;

    private BigDecimal amount;

    private Date create_time;
}