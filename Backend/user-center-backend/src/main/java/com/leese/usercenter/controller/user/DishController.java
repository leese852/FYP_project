package com.leese.usercenter.controller.user;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.model.entity.Dish;
import com.leese.usercenter.model.vo.DishVO;
import com.leese.usercenter.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("user/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public BaseResponse<List<DishVO>> getDishListByName(@RequestBody String name){

        List<DishVO> result= dishService.getDishByName(name);
        return ResultUtils.success(result);
    }
}
