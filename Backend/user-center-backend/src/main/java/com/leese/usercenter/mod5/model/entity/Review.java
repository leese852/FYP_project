package com.leese.usercenter.mod5.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "review")
public class Review implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "userId")
    private Integer userId;

    @TableField(value = "orderId")
    private Integer orderId;

    private Integer rating;

    private String comment;

    private Integer likes;

    @TableField(value = "likesUserId")
    private Integer likesUserId;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}