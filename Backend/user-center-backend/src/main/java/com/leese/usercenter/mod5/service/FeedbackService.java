package com.leese.usercenter.mod5.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leese.usercenter.mod5.model.dto.FeedbackRequest;
import com.leese.usercenter.mod5.model.dto.FeedbackResponse;
import com.leese.usercenter.mod5.model.entity.Feedback;
import java.util.List;

public interface FeedbackService {

    // 原有方法
    FeedbackResponse createFeedback(FeedbackRequest request);
    boolean deleteFeedback(Long id);
    FeedbackResponse updateFeedback(Long id, FeedbackRequest request);
    FeedbackResponse getFeedbackById(Long id);
    List<FeedbackResponse> getAllFeedbacks();
    List<FeedbackResponse> getFeedbacksByUserId(Long userId);

    // MyBatis Plus 的基础方法
    boolean update(UpdateWrapper<Feedback> updateWrapper);
}