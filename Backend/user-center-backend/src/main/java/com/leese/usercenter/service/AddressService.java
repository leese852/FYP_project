package com.leese.usercenter.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.leese.usercenter.model.dto.AddressDTO;
import com.leese.usercenter.model.entity.Address;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author leese
 * @description 针对表【address】的数据库操作Service
 * @createDate 2026-01-13 15:57:35
 */
public interface AddressService extends IService<Address> {

    void addAddress(AddressDTO addressDTO, HttpServletRequest request);

    void updateAddress(AddressDTO addressDTO, HttpServletRequest request);

    void deleteAddress(Integer addressId, HttpServletRequest request);

    /**
     * 获取地址详情
     */
    AddressDTO getAddressById(Integer addressId, HttpServletRequest request);

    /**
     * 获取用户所有地址
     */
    List<AddressDTO> getUserAddresses(HttpServletRequest request);

    boolean setDefaultAddress(AddressDTO dto, HttpServletRequest request);

}
