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

    void save(@Param("order") OrderEntity order);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    void assignRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId);
}

