package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName review
 */
@TableName(value ="review")
@Data
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