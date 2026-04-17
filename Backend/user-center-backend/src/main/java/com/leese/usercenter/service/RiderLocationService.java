package com.leese.usercenter.service;

import com.leese.usercenter.model.entity.RiderLocationHistory;

public interface RiderLocationService {
    void updateLocation(Long riderId, Double lat, Double lng, Long orderId);
    RiderLocationHistory getLatestLocationByOrder(Long orderId);
}