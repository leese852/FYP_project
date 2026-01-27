package com.leese.usercenter.mod5.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜品评分
 */

@Data
@TableName(value = "review_dish_ratings")
public class ReviewDishRating implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "review_id")
    private Long reviewId;

    @TableField(value = "dish_id")
    private Long dishId;

    private Integer rating;

    @TableField(value = "created_at")
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}