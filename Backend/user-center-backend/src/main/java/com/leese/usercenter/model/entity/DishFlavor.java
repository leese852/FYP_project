package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName dish_flavor
 */
@TableName(value ="dish_flavor")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class DishFlavor {
    private Integer id;

    private String tag;

    private String list;

    private Integer dishId;
}