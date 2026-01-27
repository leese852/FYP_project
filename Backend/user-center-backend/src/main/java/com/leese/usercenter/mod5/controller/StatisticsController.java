package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.StatisticsResponse;
import com.leese.usercenter.mod5.service.StatisticsService;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取统计数据（可能需要管理员权限）
     */
    @GetMapping("/sales")
    public StatisticsResponse getSalesStatistics(
            @RequestParam(defaultValue = "ALL") String periodType,
            HttpServletRequest request) {
        // 检查用户登录
        User currentUser = AuthUtil.checkUserLogin(request);
        // 如果需要管理员权限：AuthUtil.checkAdmin(request);

        System.out.println("获取销售统计数据，时间范围：" + periodType + "，用户ID：" + currentUser.getId());
        return statisticsService.getSalesStatistics(periodType);
    }

    /**
     * 获取今日数据
     */
    @GetMapping("/today")
    public StatisticsResponse.DailyStats getTodayStats(HttpServletRequest request) {
        AuthUtil.checkUserLogin(request);
        return statisticsService.getTodayStats();
    }

    /**
     * 获取本周数据
     */
    @GetMapping("/week")
    public StatisticsResponse.WeeklyStats getWeeklyStats(HttpServletRequest request) {
        AuthUtil.checkUserLogin(request);
        return statisticsService.getWeeklyStats();
    }

    /**
     * 获取本月数据
     */
    @GetMapping("/month")
    public StatisticsResponse.MonthlyStats getMonthlyStats(HttpServletRequest request) {
        AuthUtil.checkUserLogin(request);
        return statisticsService.getMonthlyStats();
    }
}