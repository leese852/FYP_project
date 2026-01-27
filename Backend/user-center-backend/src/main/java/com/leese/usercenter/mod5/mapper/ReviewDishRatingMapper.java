package com.leese.usercenter.mod5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.mod5.model.entity.ReviewDishRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 菜品评分 Mapper
 */
@Mapper
public interface ReviewDishRatingMapper extends BaseMapper<ReviewDishRating> {

    /**
     * 根据评价ID查询菜品评分
     */
    @Select("SELECT * FROM review_dish_ratings WHERE review_id = #{reviewId}")
    List<ReviewDishRating> selectByReviewId(Long reviewId);

    /**
     * 查询热门菜品（按订单数量）
     */
    @Select("SELECT dr.dish_id, COUNT(*) as order_count " +
            "FROM review_dish_ratings dr " +
            "INNER JOIN reviews r ON dr.review_id = r.id " +
            "INNER JOIN payment_records p ON r.order_id = p.order_id " +
            "WHERE p.status = 'SUCCESS' AND p.created_at BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY dr.dish_id " +
            "ORDER BY order_count DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectPopularDishes(String startTime, String endTime, Integer limit);
}