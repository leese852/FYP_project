package com.leese.usercenter.controller.rider;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.model.entity.RiderLocationHistory;
import com.leese.usercenter.service.RiderLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rider/location")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RiderLocationController {

    @Autowired
    private RiderLocationService riderLocationService;

    /**
     * 骑手更新位置（骑手端调用）
     */
    @PostMapping("/update")
    public BaseResponse<Void> updateLocation(@RequestBody RiderLocationHistory location) {
        // 🔥 将 Integer 转换为 Long
        Long orderIdLong = location.getOrderId() != null ? location.getOrderId().longValue() : null;

        riderLocationService.updateLocation(
                location.getRiderId(),
                location.getLat(),
                location.getLng(),
                orderIdLong
        );
        return ResultUtils.success();
    }

    /**
     * 顾客获取骑手位置（顾客端调用）
     */
    @GetMapping("/get")
    public BaseResponse<Map<String, Object>> getRiderLocation(@RequestParam Long orderId) {
        RiderLocationHistory location = riderLocationService.getLatestLocationByOrder(orderId);
        if (location == null) {
            return ResultUtils.success(new HashMap<>());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("lat", location.getLat());
        result.put("lng", location.getLng());
        result.put("updateTime", location.getCreateTime());
        return ResultUtils.success(result);
    }
}