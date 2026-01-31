package com.leese.usercenter.service;

import com.leese.usercenter.model.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author wuhao
* @description 针对表【cart】的数据库操作Service
* @createDate 2025-12-10 00:56:31
*/
public interface CartService extends IService<Cart> {
    void addCart(Cart cart,HttpServletRequest request);
    void deleteCart(int id,HttpServletRequest request);
    List<Cart> getAllCart(HttpServletRequest request);
    void deleteAllCart(HttpServletRequest request);
    void updateCart(Cart cart);
}
