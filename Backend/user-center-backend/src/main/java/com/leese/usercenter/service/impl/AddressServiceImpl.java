package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.model.Address;
import com.leese.usercenter.service.AddressService;
import com.leese.usercenter.mapper.AddressMapper;
import org.springframework.stereotype.Service;

/**
* @author leese
* @description 针对表【address】的数据库操作Service实现
* @createDate 2025-12-09 14:22:19
*/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
    implements AddressService{

}




