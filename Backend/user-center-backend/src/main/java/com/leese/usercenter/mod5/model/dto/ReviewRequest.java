package com.leese.usercenter.mod5.model.dto;

import java.util.List;

public class ReviewRequest {
    /**
     * 订单ID
     */
    private Integer orderId;

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
    private Integer userId;

    /**
     * 菜品评分内部类
     */
    public static class DishRating {
        private Long dishId;     // 保持Long（dish_id是bigint）
        private Integer rating;  // 保持Integer（rating是tinyint）

        // Getters and Setters
        public Long getDishId() { return dishId; }
        public void setDishId(Long dishId) { this.dishId = dishId; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
    }

    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<DishRating> getDishRatings() { return dishRatings; }
    public void setDishRatings(List<DishRating> dishRatings) { this.dishRatings = dishRatings; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}