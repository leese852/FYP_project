package com.leese.usercenter.mod5.service;

import com.leese.usercenter.mod5.model.dto.FeedbackRequest;
import com.leese.usercenter.mod5.model.dto.FeedbackResponse;
import java.util.List;

public interface FeedbackService {

    // 增
    FeedbackResponse createFeedback(FeedbackRequest request);

    // 删（软删除）
    boolean deleteFeedback(Long id);

    // 改
    FeedbackResponse updateFeedback(Long id, FeedbackRequest request);

    // 查
    FeedbackResponse getFeedbackById(Long id);
    List<FeedbackResponse> getAllFeedbacks();
    List<FeedbackResponse> getFeedbacksByUserId(Long userId);
}