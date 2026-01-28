package com.leese.usercenter.mod5.model.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ReviewResponse {
    private Long reviewId;
    private Integer orderId;
    private Integer userId;
    private String userName;
    private Integer rating;
    private String comment;
    private Date reviewTime;
    private List<DishRatingResponse> dishRatings;

    @Data
    public static class DishRatingResponse {
        private Long dishId;
        private String dishName;
        private Integer rating;
    }
}