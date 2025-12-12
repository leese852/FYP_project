package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.model.entity.Cart;
import com.leese.usercenter.service.CartService;
import com.leese.usercenter.mapper.CartMapper;
import org.springframework.stereotype.Service;

/**
* @author wuhao
* @description 针对表【cart】的数据库操作Service实现
* @createDate 2025-12-10 00:56:31
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService{

}




