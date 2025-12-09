package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName review
 */
@TableName(value ="review")
@Data
public class Review {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 訂單id
     */
    private Integer orderId;

    /**
     * 評分1-5，1最低
     */
    private Integer rating;

    /**
     * 評論内容
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新時間
     */
    private Date updateTime;

    /**
     * 是否删除 1-刪除
     */
    private Integer isDelete;
}