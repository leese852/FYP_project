package com.leese.usercenter.mod5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.mod5.model.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付记录 Mapper
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    @Select("SELECT * FROM payment_records WHERE order_id = #{orderId} ORDER BY created_at DESC LIMIT 1")
    PaymentRecord selectLatestByOrderId(Long orderId);

    @Select("SELECT * FROM payment_records WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<PaymentRecord> selectByUserId(Long userId);

    @Select("SELECT SUM(amount) as total_amount, COUNT(*) as order_count " +
            "FROM payment_records " +
            "WHERE status = 'SUCCESS' AND created_at BETWEEN #{startTime} AND #{endTime}")
    Map<String, Object> selectStatsByTimeRange(String startTime, String endTime);

    @Select("SELECT payment_method, SUM(amount) as total_amount " +
            "FROM payment_records " +
            "WHERE status = 'SUCCESS' AND created_at BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY payment_method")
    List<Map<String, Object>> selectPaymentMethodDistribution(String startTime, String endTime);
}