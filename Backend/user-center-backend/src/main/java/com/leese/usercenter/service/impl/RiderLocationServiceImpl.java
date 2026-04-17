package com.leese.usercenter.service.impl;

import com.leese.usercenter.mapper.RiderLocationMapper;
import com.leese.usercenter.model.entity.RiderLocationHistory;
import com.leese.usercenter.service.RiderLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RiderLocationServiceImpl implements RiderLocationService {

    @Autowired
    private RiderLocationMapper riderLocationMapper;

    @Override
    public void updateLocation(Long riderId, Double lat, Double lng, Long orderId) {
        RiderLocationHistory location = new RiderLocationHistory();
        location.setRiderId(riderId);
        // 🔥 将 Long 转换为 Integer
        location.setOrderId(orderId != null ? orderId.intValue() : null);
        location.setLat(lat);
        location.setLng(lng);
        location.setCreateTime(LocalDateTime.now());
        riderLocationMapper.insert(location);
    }

    @Override
    public RiderLocationHistory getLatestLocationByOrder(Long orderId) {
        // 🔥 将 Long 转换为 Integer
        return riderLocationMapper.findLatestByOrderId(orderId != null ? orderId.intValue() : null);
    }
}