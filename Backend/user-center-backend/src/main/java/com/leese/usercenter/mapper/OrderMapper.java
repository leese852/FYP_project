package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderEntity findById(Long id);
    List<OrderEntity> findByUserId(Long userId);
    void save(OrderEntity order);
    void updateStatus(Long id, String status);
    void assignRider(Long orderId, Long employeeId);
}
