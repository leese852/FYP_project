package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 根據訂單ID查詢訂單
     */
    OrderEntity findById(@Param("id") Long id);

    /**
     * 根據用戶ID查詢訂單列表
     */
    List<OrderEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 保存新訂單
     */
    void save(@Param("order") OrderEntity order);

    /**
     * 更新訂單狀態
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 指派騎手給訂單
     */
    void assignRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId);
}
