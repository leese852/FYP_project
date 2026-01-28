package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface OrderMapper {
    OrderEntity findById(@Param("id") Long id);

    List<OrderEntity> findByUserId(@Param("userId") Integer userId);

    List<OrderEntity> findAll(); // ✅ 新增

    /**
     * 保存訂單主表
     * 這裡不使用 @Param，直接讓 MyBatis 以實體屬性名綁定（對應 XML 中的 #{orderId}, #{userId} 等）
     */
    void save(OrderEntity order);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    void assignRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId);
}

