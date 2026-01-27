package com.leese.usercenter.mod5.service;

import com.leese.usercenter.mod5.model.dto.StatisticsResponse;

/**
 * 统计服务
 */
public interface StatisticsService {

    /**
     * 获取销售统计数据
     */
    StatisticsResponse getSalesStatistics(String periodType);

    /**
     * 获取今日统计数据
     */
    StatisticsResponse.DailyStats getTodayStats();

    /**
     * 获取本周统计数据
     */
    StatisticsResponse.WeeklyStats getWeeklyStats();

    /**
     * 获取本月统计数据
     */
    StatisticsResponse.MonthlyStats getMonthlyStats();
}