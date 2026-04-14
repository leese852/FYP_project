package com.leese.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.model.entity.Dish;
import com.leese.usercenter.model.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
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

    //更新支付状态和状态
    int updatePayStatus(@Param("id") Long id,
                        @Param("payStatus") Integer payStatus,
                        @Param("status") Integer status);

    // 根据条件查询单个订单
    OrderEntity selectOne(@Param("ew") QueryWrapper<OrderEntity> queryWrapper);

}