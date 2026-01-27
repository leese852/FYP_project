package com.leese.usercenter.mod5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.mod5.mapper.ReviewDishRatingMapper;
import com.leese.usercenter.mod5.mapper.ReviewMapper;
import com.leese.usercenter.mod5.model.dto.ReviewRequest;
import com.leese.usercenter.mod5.model.dto.ReviewResponse;
import com.leese.usercenter.mod5.model.entity.Review;
import com.leese.usercenter.mod5.model.entity.ReviewDishRating;
import com.leese.usercenter.mod5.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评价服务实现
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
        implements ReviewService {

    private final ReviewDishRatingMapper reviewDishRatingMapper;

    public ReviewServiceImpl(ReviewDishRatingMapper reviewDishRatingMapper) {
        this.reviewDishRatingMapper = reviewDishRatingMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewResponse submitReview(ReviewRequest reviewRequest) {
        // 1. 检查订单是否已评价
        Review existingReview = this.baseMapper.selectByOrderId(reviewRequest.getOrderId());
        if (existingReview != null) {
            throw new RuntimeException("该订单已评价，不能重复评价");
        }

        // 2. 保存评价（使用现有review表）
        Review review = new Review();
        review.setUserId(reviewRequest.getUserId());
        review.setOrderId(reviewRequest.getOrderId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreateTime(new Date());
        review.setUpdateTime(new Date());
        review.setIsDelete(0);
        review.setLikes(0);
        review.setLikesUserId(null);

        this.save(review);

        // 3. 保存菜品评分（新表）
        if (reviewRequest.getDishRatings() != null && !reviewRequest.getDishRatings().isEmpty()) {
            List<ReviewDishRating> dishRatings = new ArrayList<>();
            for (ReviewRequest.DishRating dishRating : reviewRequest.getDishRatings()) {
                ReviewDishRating rating = new ReviewDishRating();
                rating.setReviewId(review.getId());
                rating.setDishId(dishRating.getDishId());
                rating.setRating(dishRating.getRating());
                rating.setCreatedAt(new Date());
                dishRatings.add(rating);
            }
            // 批量保存菜品评分
            for (ReviewDishRating rating : dishRatings) {
                reviewDishRatingMapper.insert(rating);
            }
        }

        // 4. 返回响应
        return convertToResponse(review);
    }

    @Override
    public ReviewResponse getReviewByOrderId(Long orderId) {
        Review review = this.baseMapper.selectByOrderId(orderId);
        if (review == null) {
            return null;
        }
        return convertToResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        List<Review> reviews = this.baseMapper.selectByUserId(userId);
        List<ReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            responses.add(convertToResponse(review));
        }
        return responses;
    }

    @Override
    public List<ReviewResponse> getReviewsByDishId(Long dishId) {
        List<Review> reviews = this.baseMapper.selectByDishId(dishId);
        List<ReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            responses.add(convertToResponse(review));
        }
        return responses;
    }

    @Override
    public Double getDishRating(Long dishId) {
        Map<String, Object> stats = this.baseMapper.selectDishRatingStats(dishId);
        if (stats == null || stats.get("avg_rating") == null) {
            return 0.0;
        }
        // 处理不同的数据库返回值类型
        Object avgRating = stats.get("avg_rating");
        if (avgRating instanceof Number) {
            return ((Number) avgRating).doubleValue();
        } else if (avgRating instanceof String) {
            return Double.parseDouble((String) avgRating);
        }
        return 0.0;
    }

    /**
     * 将 Review 实体转换为 ReviewResponse
     */
    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setReviewId(review.getId());
        response.setOrderId(review.getOrderId());
        response.setUserId(review.getUserId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setReviewTime(review.getCreateTime());

        // 获取菜品评分
        List<ReviewDishRating> dishRatings = reviewDishRatingMapper.selectByReviewId(review.getId());
        List<ReviewResponse.DishRatingResponse> dishRatingResponses = new ArrayList<>();

        for (ReviewDishRating dishRating : dishRatings) {
            ReviewResponse.DishRatingResponse dishRatingResponse = new ReviewResponse.DishRatingResponse();
            dishRatingResponse.setDishId(dishRating.getDishId());
            dishRatingResponse.setRating(dishRating.getRating());
            // TODO: 需要从菜品模块获取菜品名称
            dishRatingResponse.setDishName("菜品" + dishRating.getDishId());
            dishRatingResponses.add(dishRatingResponse);
        }

        response.setDishRatings(dishRatingResponses);
        return response;
    }
}