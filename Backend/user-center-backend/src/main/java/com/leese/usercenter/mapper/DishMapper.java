package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
* @author leese
* @description 针对表【dish】的数据库操作Mapper
* @createDate 2025-12-09 14:22:19
* @Entity com.leese.usercenter.model.entity.Dish
*/
public interface DishMapper extends BaseMapper<Dish> {
    
    @Select("SELECT d.* FROM dish d " +
            "LEFT JOIN order_items oi ON d.id = oi.dishId " +
            "WHERE d.isAvailable = 1 AND d.isDelete = 0 " +
            "GROUP BY d.id " +
            "ORDER BY COALESCE(SUM(oi.quantity), 0) DESC " +
            "LIMIT 10")
    List<Dish> getTopRecommendedDishes();
}




