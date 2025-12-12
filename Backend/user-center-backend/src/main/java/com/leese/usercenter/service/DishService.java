package com.leese.usercenter.service;

import com.leese.usercenter.model.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.model.vo.DishVO;

import java.util.List;

/**
* @author leese
* @description 针对表【dish】的数据库操作Service
* @createDate 2025-12-09 14:22:19
*/
public interface DishService extends IService<Dish> {
    void addDish(DishDTO dish);
    void updateDish(DishDTO dish);
    void onOff(int id);
    void deleteBatch(List<Integer> ids);
    List<DishVO> getDishByName(String DishName);
//    List<DishVO> getAllDish();
    List<DishVO> getDishByCategory(int category);

}
