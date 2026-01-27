package com.leese.usercenter.mod5.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 评价响应
 */
@Data
public class ReviewResponse {
    private Long reviewId;
    private Long orderId;
    private Long userId;
    private String userName;
    private Integer rating;  // 改为 rating
    private String comment;  // 改为 comment
    private Date reviewTime;
    private List<DishRatingResponse> dishRatings;

    @Data
    public static class DishRatingResponse {
        private Long dishId;
        private String dishName;
        private Integer rating;
    }
}