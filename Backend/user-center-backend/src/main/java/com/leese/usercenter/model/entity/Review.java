package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName review
 */
@TableName(value ="review")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class Review {
    private Integer id;

    private Integer userId;

    private Integer orderId;

    private Integer rating;

    private String comment;

    private Integer likes;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;
}