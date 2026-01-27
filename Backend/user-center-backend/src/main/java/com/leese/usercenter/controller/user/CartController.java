package com.leese.usercenter.controller.user;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.model.entity.Cart;
import com.leese.usercenter.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController()
@RequestMapping("user/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public BaseResponse<String> addCart(@RequestBody Cart cart, HttpServletRequest request){
        cartService.addCart(cart,request);
        return ResultUtils.success();
    }

    @DeleteMapping("/delete")
    public BaseResponse<String> deleteCart(int id, HttpServletRequest request){
        cartService.deleteCart(id,request);
        return ResultUtils.success();
    }

    @GetMapping("/getAll")
    public BaseResponse<List<Cart>> getAllCart(HttpServletRequest request){
        return ResultUtils.success(cartService.getAllCart(request));
    }

    @DeleteMapping("/deleteAll")
    public BaseResponse<String> deleteAllCart(HttpServletRequest request){
        cartService.deleteAllCart(request);
        return ResultUtils.success();
    }
}
