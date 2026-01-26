package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    List<OrderItemEntity> findByOrderId(Long orderId);
}
