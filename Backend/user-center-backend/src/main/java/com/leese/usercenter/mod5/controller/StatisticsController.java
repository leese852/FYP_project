package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.StatisticsResponse;
import com.leese.usercenter.mod5.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取统计数据
     */
    @GetMapping("/sales")
    public StatisticsResponse getSalesStatistics(
            @RequestParam(defaultValue = "ALL") String periodType) {
        System.out.println("获取销售统计数据，时间范围：" + periodType);
        return statisticsService.getSalesStatistics(periodType);
    }

    /**
     * 获取今日数据
     */
    @GetMapping("/today")
    public StatisticsResponse.DailyStats getTodayStats() {
        return statisticsService.getTodayStats();
    }

    /**
     * 获取本周数据
     */
    @GetMapping("/week")
    public StatisticsResponse.WeeklyStats getWeeklyStats() {
        return statisticsService.getWeeklyStats();
    }

    /**
     * 获取本月数据
     */
    @GetMapping("/month")
    public StatisticsResponse.MonthlyStats getMonthlyStats() {
        return statisticsService.getMonthlyStats();
    }
}