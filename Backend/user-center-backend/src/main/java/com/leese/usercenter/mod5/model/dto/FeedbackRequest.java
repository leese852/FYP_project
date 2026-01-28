package com.leese.usercenter.mod5.model.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long userId;      // 可以从token获取，这里可选
    private String content;   // 反馈内容
    private String type;      // 反馈类型
}