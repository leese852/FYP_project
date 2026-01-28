package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    /**
     * 根據訂單主鍵 ID 查詢訂單項列表
     */
    List<OrderItemEntity> findByOrderId(Long orderId);

    /**
     * 新增單條訂單項記錄
     */
    void insert(OrderItemEntity orderItem);
}
