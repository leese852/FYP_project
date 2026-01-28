package com.leese.usercenter.mod5.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FeedbackResponse {
    private Long id;
    private Long userId;
    private String userName;  // 可能需要关联查询
    private String content;
    private String type;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}