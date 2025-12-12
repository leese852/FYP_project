package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.constant.StatusConstant;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.mapper.DishFlavorMapper;
import com.leese.usercenter.model.entity.Dish;
import com.leese.usercenter.model.entity.DishFlavor;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.model.vo.DishVO;
import com.leese.usercenter.service.DishFlavorService;
import com.leese.usercenter.service.DishService;
import com.leese.usercenter.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
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
    public List<DishVO> getDishByName(String DishName) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        // 如果搜索关键词为空，返回空列表，避免全表扫描
        if (DishName != null && !DishName.trim().isEmpty()) {
            queryWrapper.like("dishName",DishName.trim());
        }
        queryWrapper.eq("isDelete",0);
        queryWrapper.eq("isAvailable",1);
        queryWrapper.orderByAsc("createTime");
        List<Dish>dishList = this.list(queryWrapper);
        return convertToVO(dishList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) //回滚功能
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //先更新Dish table
        boolean success = this.updateById(dish);
        if(!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"Dish更新失败");
        }else{
            int dishId = dish.getId();
            // 删除原有口味（使用批量删除）
            QueryWrapper<DishFlavor> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("dishId",dishId);
            //刪除同id的flavor
            dishFlavorService.remove(deleteWrapper);
//            dishFlavorService.remove(new QueryWrapper<DishFlavor>().eq("dishId",dishId));
            List<DishFlavor> flavorList = dishDTO.getFlavors();
            // 添加新口味（使用批量插入）
            if(flavorList != null && !flavorList.isEmpty()){
                for (DishFlavor dishFlavor : flavorList) {
                    dishFlavor.setDishId(dishId);
                    dishFlavorMapper.insert(dishFlavor);
                }
            }
        }
    }

    @Override
    public void onOff(int id) {
        Dish dish = this.getById(id);
        // 2. 切换状态：1变0，0变1
        int currentStatus = dish.getIsAvailable();
        int newStatus = (currentStatus == 0) ? 1 : 0;
        // 3. 更新状态
        dish.setIsAvailable(newStatus);
        boolean success = this.updateById(dish);
        if(!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"状态更新失敗");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        for(int id : ids){
            Dish dish = this.getById(id);
            if(dish.getIsAvailable() == StatusConstant.ENABLE){
                throw new BusinessException(ErrorCode.DISH_STATUS_ERROR,"菜品:" + dish.getDishName() + "菜品狀態為啓用");
            }
        }
        boolean dishSuccess = this.removeBatchByIds(ids);
        if(!dishSuccess){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"菜品删除失败");
        }
        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.in("dishId",ids);
        dishFlavorService.remove(qw);
    }

//    @Override
//    public List<DishVO>getAllDish(){
//        QueryWrapper<Dish> qw = new QueryWrapper<>();
//        qw.eq("isDelete",StatusConstant.NOT_DELETED);
//        qw.eq("isAvailable",StatusConstant.ENABLE);
//        qw.orderByAsc("createTime");
//        List<Dish> dishList = this.list(qw);
//        return convertToVO(dishList);
//    }

    @Override
    public List<DishVO>getDishByCategory(int category){
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.eq("isDelete",StatusConstant.NOT_DELETED);
        qw.eq("isAvailable",StatusConstant.ENABLE);
        qw.eq("categoryId",category);
        qw.orderByAsc("createTime");
        List<Dish> dishList = this.list(qw);
        return convertToVO(dishList);
    }

    public List<DishVO> convertToVO(List<Dish> dishList){
        List<DishVO> voList = new ArrayList<>();
        for(Dish d : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            // 查询该菜品对应的口味选项
            QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
            qw.eq("dishId",d.getId());
            List<DishFlavor> flavors = dishFlavorService.list(qw);
            dishVO.setFlavors(flavors);
            voList.add(dishVO);
        }
        return voList;
    }

}




