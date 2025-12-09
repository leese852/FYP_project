package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName dish_flavor
 */
@TableName(value ="dish_flavor")
@Data
public class DishFlavor {
    private Integer id;

    private String tag;

    private String list;

    private Integer dishId;
}