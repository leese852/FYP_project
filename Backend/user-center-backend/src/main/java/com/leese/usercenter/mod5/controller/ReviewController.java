package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.ReviewRequest;
import com.leese.usercenter.mod5.model.dto.ReviewResponse;
import com.leese.usercenter.mod5.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 提交评价
     */
    @PostMapping("/submit")
    public ReviewResponse submitReview(@RequestBody ReviewRequest reviewRequest) {
        // 临时设置用户ID（测试用）
        reviewRequest.setUserId(1L);

        // 手动验证
        if (reviewRequest.getOrderId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (reviewRequest.getRating() == null || reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }

        return reviewService.submitReview(reviewRequest);
    }

    /**
     * 根据订单ID查询评价
     */
    @GetMapping("/order/{orderId}")
    public ReviewResponse getReviewByOrderId(@PathVariable Long orderId) {
        return reviewService.getReviewByOrderId(orderId);
    }

    /**
     * 根据用户ID查询评价列表
     */
    @GetMapping("/user/{userId}")
    public List<ReviewResponse> getReviewsByUserId(@PathVariable Long userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    /**
     * 根据菜品ID查询评价列表
     */
    @GetMapping("/dish/{dishId}")
    public List<ReviewResponse> getReviewsByDishId(@PathVariable Long dishId) {
        return reviewService.getReviewsByDishId(dishId);
    }

    /**
     * 获取菜品评分
     */
    @GetMapping("/dish/{dishId}/rating")
    public Double getDishRating(@PathVariable Long dishId) {
        return reviewService.getDishRating(dishId);
    }
}