package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.model.Orders;
import com.leese.usercenter.service.OrdersService;
import com.leese.usercenter.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author leese
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2025-12-09 14:22:19
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




