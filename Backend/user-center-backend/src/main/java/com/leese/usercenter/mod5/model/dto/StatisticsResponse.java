package com.leese.usercenter.mod5.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计数据响应
 */
@Data
public class StatisticsResponse {
    /**
     * 今日统计数据
     */
    private DailyStats todayStats;

    /**
     * 本周统计数据
     */
    private WeeklyStats weeklyStats;

    /**
     * 本月统计数据
     */
    private MonthlyStats monthlyStats;

    /**
     * 热门菜品统计
     */
    private List<DishStats> popularDishes;

    /**
     * 支付方式分布
     */
    private Map<String, BigDecimal> paymentMethodDistribution;

    /**
     * 评价统计
     */
    private ReviewStats reviewStats;

    @Data
    public static class DailyStats {
        private Integer orderCount;
        private BigDecimal totalAmount;
        private BigDecimal avgOrderAmount;
    }

    @Data
    public static class WeeklyStats {
        private Integer orderCount;
        private BigDecimal totalAmount;
        private List<DailyData> dailyData;
    }

    @Data
    public static class MonthlyStats {
        private Integer orderCount;
        private BigDecimal totalAmount;
        private List<WeeklyData> weeklyData;
    }

    @Data
    public static class DailyData {
        private String date;
        private Integer orderCount;
        private BigDecimal totalAmount;
    }

    @Data
    public static class WeeklyData {
        private String week;
        private Integer orderCount;
        private BigDecimal totalAmount;
    }

    @Data
    public static class DishStats {
        private Long dishId;
        private String dishName;
        private Integer orderCount;
        private BigDecimal totalAmount;
        private Double avgRating;
    }

    @Data
    public static class ReviewStats {
        private Double avgRating;
        private Integer totalReviews;
        private Map<Integer, Integer> ratingDistribution; // 评分分布
    }
}