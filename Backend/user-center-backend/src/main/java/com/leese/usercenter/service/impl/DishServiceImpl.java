package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.mapper.DishFlavorMapper;
import com.leese.usercenter.model.Dish;
import com.leese.usercenter.model.DishFlavor;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.service.DishFlavorService;
import com.leese.usercenter.service.DishService;
import com.leese.usercenter.mapper.DishMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author leese
* @description 针对表【dish】的数据库操作Service实现
* @createDate 2025-12-09 14:22:19
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{
    @Autowired
    DishMapper dishMapper;
    DishFlavorMapper dishFlavorMapper;
    DishFlavorService dishFlavorService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        //复制相同名称的属性
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setId(null);
//        dishMapper.insert(dish); 原生 MyBatis
        boolean success = this.save(dish); //MyBatis-Plus
        if(!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"Dish插入失败");
        }else{
            int dishId = dish.getId();
            // 有了DTO中的flavors，加上上面的dish_id，就可以批量插入口味数据到dish_flavor表了
            List<DishFlavor> flavorList = dishDTO.getFlavors();
            if(flavorList != null && !flavorList.isEmpty()){
                flavorList.forEach(flavor -> flavor.setDishId(dishId));
                dishFlavorService.saveBatch(flavorList);
            }
        }
    }

    @Override
    public List<Dish> searchDishByName(String DishName) {
        return List.of();
    }

    @Override
    public void updateDish(Dish dish) {

    }

    @Override
    public void onOff(int id) {

    }

    @Override
    public void deleteBatch(List<Integer> ids) {

    }
}




