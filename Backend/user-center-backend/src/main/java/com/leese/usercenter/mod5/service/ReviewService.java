package com.leese.usercenter.mod5.service;

import com.leese.usercenter.mod5.model.dto.ReviewRequest;
import com.leese.usercenter.mod5.model.dto.ReviewResponse;
import com.leese.usercenter.mod5.model.entity.Review;

import java.util.List;

/**
 * 评价服务
 */
public interface ReviewService {

    /**
     * 提交评价
     */
    ReviewResponse submitReview(ReviewRequest reviewRequest);

    /**
     * 根据订单ID查询评价
     */
    ReviewResponse getReviewByOrderId(Long orderId);

    /**
     * 根据用户ID查询评价列表
     */
    List<ReviewResponse> getReviewsByUserId(Long userId);

    /**
     * 根据菜品ID查询评价列表
     */
    List<ReviewResponse> getReviewsByDishId(Long dishId);

    /**
     * 获取菜品评分统计
     */
    Double getDishRating(Long dishId);  // 确保这个方法存在
}