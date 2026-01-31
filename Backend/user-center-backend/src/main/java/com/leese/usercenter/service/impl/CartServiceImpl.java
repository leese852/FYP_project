package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.entity.Cart;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.CartService;
import com.leese.usercenter.mapper.CartMapper;
import com.leese.usercenter.utils.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
* @author wuhao
* @description 针对表【cart】的数据库操作Service实现
* @createDate 2025-12-10 00:56:31
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService{
    @Autowired
    CartMapper cartMapper;

    @Override
    public List<Cart> getAllCart(HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        return cartMapper.selectList(new QueryWrapper<Cart>().eq("userId",user.getId()));
    }

    @Override
    public void updateCart(Cart cart) {
        if (cart == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "购物车数据不能为空");
        }

        if (cart.getId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "购物车ID不能为空");
        }

        if (cart.getNumber() == null || cart.getNumber() <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "商品数量必须大于0");
        }
        Cart update = new Cart();
        update.setId(cart.getId());
        update.setNumber(cart.getNumber());
        boolean success = this.updateById(update);
        if(!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"Dish更新失败");
        }
    }

    @Override
    public void deleteCart(int id,HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        Cart cart = cartMapper.selectById(id);
        if(Objects.equals(cart.getUserId(), user.getId())){
            this.removeById(id);
        }
    }

    @Override
    public void addCart(Cart cart,HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",user.getId())
                .eq("dishId",cart.getDishId());
        //查看菜品口味是否存在。存在加入匹配规则
        if(cart.getDishFlavor()!= null){
            queryWrapper.eq("dishFlavor",cart.getDishFlavor());
        }
        //查看用户是否有该商品
        Cart checkCart = this.getOne(queryWrapper);
        if(checkCart != null){
            checkCart.setNumber(checkCart.getNumber()+cart.getNumber());
            this.updateById(checkCart);
        }else{
            cart.setUserId(user.getId());
            this.save(cart);
        }
    }

    @Override
    public void deleteAllCart(HttpServletRequest request){
        User user = AuthUtil.checkUserLogin(request);
        this.remove(new QueryWrapper<Cart>().eq("userId",user.getId()));
    }
}




