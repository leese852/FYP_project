package com.leese.usercenter.mod5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.mod5.model.entity.Review;
import com.leese.usercenter.mod5.model.dto.StatisticsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 评价 Mapper
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 根据订单ID查询评价
     */
    @Select("SELECT * FROM review WHERE orderId = #{orderId} AND isDelete = 0")
    Review selectByOrderId(Long orderId);

    /**
     * 根据用户ID查询评价
     */
    @Select("SELECT * FROM review WHERE userId = #{userId} AND isDelete = 0 ORDER BY createTime DESC")
    List<Review> selectByUserId(Long userId);

    /**
     * 根据菜品ID查询评价
     */
    @Select("SELECT r.* FROM review r " +
            "INNER JOIN review_dish_ratings dr ON r.id = dr.review_id " +
            "WHERE dr.dish_id = #{dishId} AND r.isDelete = 0 " +
            "ORDER BY r.createTime DESC")
    List<Review> selectByDishId(Long dishId);

    /**
     * 查询菜品的平均评分
     */
    @Select("SELECT AVG(dr.rating) as avg_rating, COUNT(*) as rating_count " +
            "FROM review_dish_ratings dr " +
            "INNER JOIN review r ON dr.review_id = r.id " +
            "WHERE dr.dish_id = #{dishId} AND r.isDelete = 0")
    Map<String, Object> selectDishRatingStats(Long dishId);

    /**
     * 获取评价统计数据
     */
    @Select("SELECT " +
            "AVG(rating) as avg_rating, " +
            "COUNT(*) as total_reviews, " +
            "SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END) as five_star, " +
            "SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END) as four_star, " +
            "SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END) as three_star, " +
            "SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END) as two_star, " +
            "SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END) as one_star " +
            "FROM review " +
            "WHERE isDelete = 0 AND createTime BETWEEN #{startTime} AND #{endTime}")
    Map<String, Object> selectReviewStats(String startTime, String endTime);
}