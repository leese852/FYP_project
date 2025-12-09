package com.leese.usercenter.service;

import com.leese.usercenter.model.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leese.usercenter.model.dto.DishDTO;

import java.util.List;

/**
* @author leese
* @description 针对表【dish】的数据库操作Service
* @createDate 2025-12-09 14:22:19
*/
public interface DishService extends IService<Dish> {
    void addDish(DishDTO dish);
    List<Dish> searchDishByName(String DishName);
    void updateDish(Dish dish);
    void onOff(int id);
    void deleteBatch(List<Integer> ids);

}
