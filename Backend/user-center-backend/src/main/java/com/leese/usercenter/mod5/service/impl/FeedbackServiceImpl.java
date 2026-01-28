package com.leese.usercenter.mod5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.mod5.mapper.FeedbackMapper;
import com.leese.usercenter.mod5.model.dto.FeedbackRequest;
import com.leese.usercenter.mod5.model.dto.FeedbackResponse;
import com.leese.usercenter.mod5.model.entity.Feedback;
import com.leese.usercenter.mod5.service.FeedbackService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback>
        implements FeedbackService {

    @Override
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(request, feedback);

        feedback.setStatus("PENDING");
        feedback.setCreatedAt(new Date());
        feedback.setIsDeleted(0);

        this.save(feedback);
        return convertToResponse(feedback);
    }

    @Override
    public boolean deleteFeedback(Long id) {
        Feedback feedback = this.getById(id);
        if (feedback == null) return false;

        feedback.setIsDeleted(1);
        feedback.setUpdatedAt(new Date());
        return this.updateById(feedback);
    }

    @Override
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedback = this.getById(id);
        if (feedback == null) return null;

        BeanUtils.copyProperties(request, feedback);
        feedback.setUpdatedAt(new Date());

        this.updateById(feedback);
        return convertToResponse(feedback);
    }

    @Override
    public FeedbackResponse getFeedbackById(Long id) {
        Feedback feedback = this.getById(id);
        return feedback != null ? convertToResponse(feedback) : null;
    }

    @Override
    public List<FeedbackResponse> getAllFeedbacks() {
        return this.list().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponse> getFeedbacksByUserId(Long userId) {
        return this.baseMapper.selectByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        BeanUtils.copyProperties(feedback, response);
        // 这里可以设置userName，如果需要关联用户表
        return response;
    }
}