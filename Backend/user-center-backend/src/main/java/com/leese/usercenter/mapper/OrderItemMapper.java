package com.leese.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.model.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {
    /**
     * 根據訂單主鍵 ID 查詢訂單項列表
     */
    List<OrderItemEntity> findByOrderId(Long orderId);

    /**
     * 新增單條訂單項記錄
     *
     * @return
     */
     int insert(OrderItemEntity orderItem);
}
