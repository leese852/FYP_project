package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderEntity findById(@Param("id") Long id);

    List<OrderEntity> findByUserId(@Param("userId") Integer userId);

    List<OrderEntity> findAll();

    /**
     * 保存訂單主表
     */
    void save(OrderEntity order);

    /**
     * 更新訂單狀態
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    void assignRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId);

    /**
     * 查詢訂單狀態（新增）
     */
    Integer findStatusById(@Param("id") Long id);

    /**
     * 檢查訂單是否存在（新增）
     */
    boolean existsById(@Param("id") Long id);
}