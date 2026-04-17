package com.leese.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.model.entity.RiderLocationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RiderLocationMapper extends BaseMapper<RiderLocationHistory> {

    // 🔥 参数类型改为 Integer
    @Select("SELECT * FROM rider_location_history WHERE order_id = #{orderId} ORDER BY create_time DESC LIMIT 1")
    RiderLocationHistory findLatestByOrderId(@Param("orderId") Integer orderId);
}