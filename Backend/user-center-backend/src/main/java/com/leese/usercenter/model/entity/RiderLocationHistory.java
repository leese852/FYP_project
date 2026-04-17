package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rider_location_history")
public class RiderLocationHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long riderId;
    private Integer orderId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createTime;

    // 为了方便使用，添加 getter/setter 别名
    public Double getLat() {
        return latitude;
    }

    public void setLat(Double lat) {
        this.latitude = lat;
    }

    public Double getLng() {
        return longitude;
    }

    public void setLng(Double lng) {
        this.longitude = lng;
    }
}