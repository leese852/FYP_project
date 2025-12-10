package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName cart
 */
@TableName(value ="cart")
@Data
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