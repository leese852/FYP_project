package com.leese.usercenter.mod5.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("feedback")
public class Feedback {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String content;
    private String type;  // 类型：SUGGESTION, COMPLAINT, PRAISE
    private String status; // 状态：PENDING, PROCESSED, RESOLVED

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("is_deleted")
    private Integer isDeleted;
}