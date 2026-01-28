package com.leese.usercenter.mod5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leese.usercenter.mod5.model.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

    @Select("SELECT * FROM feedback WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY created_at DESC")
    List<Feedback> selectByUserId(Long userId);

    @Select("SELECT * FROM feedback WHERE status = #{status} AND is_deleted = 0 ORDER BY created_at DESC")
    List<Feedback> selectByStatus(String status);
}