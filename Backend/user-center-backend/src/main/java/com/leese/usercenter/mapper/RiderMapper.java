package com.leese.usercenter.mapper;

import com.leese.usercenter.model.entity.RiderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RiderMapper {
    RiderEntity findById(Long employeeId);
}
