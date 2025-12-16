package com.leese.usercenter.controller.admin;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.model.entity.Dish;
import com.leese.usercenter.model.vo.DishVO;
import com.leese.usercenter.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j //log注解api
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController("adminDishController")
@RequestMapping("admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("/add_dish")
    public BaseResponse addDish(@RequestBody DishDTO dishDTO){
        log.info("要添加的菜品信息：{}", dishDTO);
        dishService.addDish(dishDTO);
        return ResultUtils.success();
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @PutMapping("/update_dish")
    public BaseResponse updateDish(@RequestBody DishDTO dishDTO){
        log.info("用户传过来的新菜品数据，{}", dishDTO);
        dishService.updateDish(dishDTO);
        return ResultUtils.success();
    }

    /**
     * 获取菜品
     * @param dishName
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<DishVO>> getDishByName(@RequestBody String dishName){
        log.info("用户传进来的dish name,{}",dishName);
        List<DishVO> result = dishService.getDishByName(dishName);

        return ResultUtils.success(result);
    }

    @GetMapping
    public BaseResponse<DishVO> getDishById(@RequestParam int id){
        DishVO result = dishService.getDishById(id);
        return ResultUtils.success(result);
    }

    @PutMapping("/status")
    public BaseResponse onOff(@RequestBody int id){
        log.info("用户传过来的用户id，{}", id);
        dishService.onOff(id);
        return ResultUtils.success();
    }

    @DeleteMapping
    public BaseResponse deleteBatch(@RequestBody List<Integer> ids){
        log.info("用户传过来的用户id_list，{}", ids);
        dishService.deleteBatch(ids);
        return ResultUtils.success();
    }
}
