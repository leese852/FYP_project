package com.leese.usercenter.mod5.model.dto;

import java.util.List;

/**
 * 评价请求
 */
public class ReviewRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 评分（1-5星）
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String comment;

    /**
     * 菜品评分列表
     */
    private List<DishRating> dishRatings;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 菜品评分内部类
     */
    public static class DishRating {
        private Long dishId;
        private Integer rating;

        // Getters and Setters
        public Long getDishId() { return dishId; }
        public void setDishId(Long dishId) { this.dishId = dishId; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<DishRating> getDishRatings() { return dishRatings; }
    public void setDishRatings(List<DishRating> dishRatings) { this.dishRatings = dishRatings; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}