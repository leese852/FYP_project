package com.leese.usercenter.mod5.service.impl;

import com.leese.usercenter.mod5.mapper.PaymentRecordMapper;
import com.leese.usercenter.mod5.mapper.ReviewDishRatingMapper;
import com.leese.usercenter.mod5.mapper.ReviewMapper;
import com.leese.usercenter.mod5.model.dto.StatisticsResponse;
import com.leese.usercenter.mod5.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 统计服务实现
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final PaymentRecordMapper paymentRecordMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewDishRatingMapper reviewDishRatingMapper;

    public StatisticsServiceImpl(PaymentRecordMapper paymentRecordMapper,
                                 ReviewMapper reviewMapper,
                                 ReviewDishRatingMapper reviewDishRatingMapper) {
        this.paymentRecordMapper = paymentRecordMapper;
        this.reviewMapper = reviewMapper;
        this.reviewDishRatingMapper = reviewDishRatingMapper;
    }

    @Override
    public StatisticsResponse getSalesStatistics(String periodType) {
        StatisticsResponse response = new StatisticsResponse();

        switch (periodType.toUpperCase()) {
            case "TODAY":
                response.setTodayStats(getTodayStats());
                break;
            case "WEEK":
                response.setWeeklyStats(getWeeklyStats());
                break;
            case "MONTH":
                response.setMonthlyStats(getMonthlyStats());
                break;
            case "ALL":
            default:
                response.setTodayStats(getTodayStats());
                response.setWeeklyStats(getWeeklyStats());
                response.setMonthlyStats(getMonthlyStats());
                break;
        }

        // 设置热门菜品
        response.setPopularDishes(getPopularDishes());

        // 设置支付方式分布
        response.setPaymentMethodDistribution(getPaymentMethodDistribution());

        // 设置评价统计
        response.setReviewStats(getReviewStats());

        return response;
    }

    @Override
    public StatisticsResponse.DailyStats getTodayStats() {
        LocalDate today = LocalDate.now();
        String startTime = today.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> stats = paymentRecordMapper.selectStatsByTimeRange(startTime, endTime);

        StatisticsResponse.DailyStats dailyStats = new StatisticsResponse.DailyStats();
        dailyStats.setOrderCount(stats.get("order_count") != null ? ((Number) stats.get("order_count")).intValue() : 0);
        dailyStats.setTotalAmount(stats.get("total_amount") != null ?
                new BigDecimal(stats.get("total_amount").toString()) : BigDecimal.ZERO);

        if (dailyStats.getOrderCount() > 0) {
            dailyStats.setAvgOrderAmount(dailyStats.getTotalAmount()
                    .divide(BigDecimal.valueOf(dailyStats.getOrderCount()), 2, RoundingMode.HALF_UP));
        } else {
            dailyStats.setAvgOrderAmount(BigDecimal.ZERO);
        }

        return dailyStats;
    }

    @Override
    public StatisticsResponse.WeeklyStats getWeeklyStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(6); // 最近7天

        List<StatisticsResponse.DailyData> dailyDataList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalOrderCount = 0;

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            String startTime = date.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endTime = date.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Map<String, Object> dayStats = paymentRecordMapper.selectStatsByTimeRange(startTime, endTime);

            StatisticsResponse.DailyData dailyData = new StatisticsResponse.DailyData();
            dailyData.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dailyData.setOrderCount(dayStats.get("order_count") != null ?
                    ((Number) dayStats.get("order_count")).intValue() : 0);
            dailyData.setTotalAmount(dayStats.get("total_amount") != null ?
                    new BigDecimal(dayStats.get("total_amount").toString()) : BigDecimal.ZERO);

            dailyDataList.add(dailyData);

            totalOrderCount += dailyData.getOrderCount();
            totalAmount = totalAmount.add(dailyData.getTotalAmount());
        }

        StatisticsResponse.WeeklyStats weeklyStats = new StatisticsResponse.WeeklyStats();
        weeklyStats.setOrderCount(totalOrderCount);
        weeklyStats.setTotalAmount(totalAmount);
        weeklyStats.setDailyData(dailyDataList);

        return weeklyStats;
    }

    @Override
    public StatisticsResponse.MonthlyStats getMonthlyStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // 获取本月数据
        String startTime = startOfMonth.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> monthStats = paymentRecordMapper.selectStatsByTimeRange(startTime, endTime);

        StatisticsResponse.MonthlyStats monthlyStats = new StatisticsResponse.MonthlyStats();
        monthlyStats.setOrderCount(monthStats.get("order_count") != null ?
                ((Number) monthStats.get("order_count")).intValue() : 0);
        monthlyStats.setTotalAmount(monthStats.get("total_amount") != null ?
                new BigDecimal(monthStats.get("total_amount").toString()) : BigDecimal.ZERO);

        // 这里简化处理，实际应该按周分组
        List<StatisticsResponse.WeeklyData> weeklyDataList = new ArrayList<>();
        // TODO: 实现按周分组逻辑
        monthlyStats.setWeeklyData(weeklyDataList);

        return monthlyStats;
    }

    /**
     * 获取热门菜品
     */
    private List<StatisticsResponse.DishStats> getPopularDishes() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(7);
        String startTime = startOfWeek.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<Map<String, Object>> popularDishes = reviewDishRatingMapper
                .selectPopularDishes(startTime, endTime, 10);

        List<StatisticsResponse.DishStats> dishStatsList = new ArrayList<>();
        for (Map<String, Object> dish : popularDishes) {
            StatisticsResponse.DishStats dishStats = new StatisticsResponse.DishStats();
            dishStats.setDishId(((Number) dish.get("dish_id")).longValue());
            dishStats.setOrderCount(((Number) dish.get("order_count")).intValue());
            // TODO: 需要从菜品模块获取菜品名称
            dishStats.setDishName("菜品" + dish.get("dish_id"));
            dishStatsList.add(dishStats);
        }

        return dishStatsList;
    }

    /**
     * 获取支付方式分布
     */
    private Map<String, BigDecimal> getPaymentMethodDistribution() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        String startTime = startOfMonth.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<Map<String, Object>> distribution = paymentRecordMapper
                .selectPaymentMethodDistribution(startTime, endTime);

        Map<String, BigDecimal> result = new HashMap<>();
        for (Map<String, Object> item : distribution) {
            String method = (String) item.get("payment_method");
            BigDecimal amount = new BigDecimal(item.get("total_amount").toString());
            result.put(method, amount);
        }

        return result;
    }

    /**
     * 获取评价统计
     */
    private StatisticsResponse.ReviewStats getReviewStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        String startTime = startOfMonth.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTime = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> reviewStats = reviewMapper.selectReviewStats(startTime, endTime);

        StatisticsResponse.ReviewStats stats = new StatisticsResponse.ReviewStats();

        if (reviewStats.get("avg_rating") != null) {
            stats.setAvgRating(((Number) reviewStats.get("avg_rating")).doubleValue());
        }

        stats.setTotalReviews(reviewStats.get("total_reviews") != null ?
                ((Number) reviewStats.get("total_reviews")).intValue() : 0);

        // 设置评分分布
        Map<Integer, Integer> ratingDistribution = new HashMap<>();
        ratingDistribution.put(5, reviewStats.get("five_star") != null ?
                ((Number) reviewStats.get("five_star")).intValue() : 0);
        ratingDistribution.put(4, reviewStats.get("four_star") != null ?
                ((Number) reviewStats.get("four_star")).intValue() : 0);
        ratingDistribution.put(3, reviewStats.get("three_star") != null ?
                ((Number) reviewStats.get("three_star")).intValue() : 0);
        ratingDistribution.put(2, reviewStats.get("two_star") != null ?
                ((Number) reviewStats.get("two_star")).intValue() : 0);
        ratingDistribution.put(1, reviewStats.get("one_star") != null ?
                ((Number) reviewStats.get("one_star")).intValue() : 0);

        stats.setRatingDistribution(ratingDistribution);

        return stats;
    }
}