package com.leese.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.dto.AddressDTO;
import com.leese.usercenter.model.entity.Address;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.AddressService;
import com.leese.usercenter.mapper.AddressMapper;
import com.leese.usercenter.utils.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.leese.usercenter.constant.StatusConstant.*;

@Slf4j
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public void addAddress(AddressDTO dto, HttpServletRequest request) {
        User curUser = AuthUtil.checkUserLogin(request);
        Address add = new Address();
        BeanUtils.copyProperties(dto, add);
        add.setUserId(curUser.getId());
        add.setId(null);
        int isDefault = add.getIsDefault();
        if (isDefault == 1) {
            cancelDefaultAddress(curUser.getId());
        }
        boolean saved = this.save(add);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    private void cancelDefaultAddress(int userId) {
        UpdateWrapper<Address> wrapper = new UpdateWrapper<>();
        wrapper.eq("userId", userId)
                .eq("isDelete", NOT_DELETED)
                .eq("isDefault", DEFAULT)
                .set("isDefault", NOT_DEFAULT);
        this.update(wrapper);
    }

    @Override
    public void updateAddress(AddressDTO dto, HttpServletRequest request) {
        User curUser = AuthUtil.checkUserLogin(request);
        Address add = this.getById(dto.getId());
        BeanUtils.copyProperties(dto, add);
        if (add.getIsDefault() == 1) {
            cancelDefaultAddress(curUser.getId());
        }
        boolean updated = this.updateById(add);
        if (!updated) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "地址更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Integer addressId, HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        log.info("删除地址 - 用户ID: {}, 地址ID: {}", user.getId(), addressId);

        // 检查地址是否存在且属于当前用户且未删除
        Address add = this.lambdaQuery()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, user.getId())
                .eq(Address::getIsDelete, NOT_DELETED)
                .one();

        if (add == null) {
            log.warn("地址不存在或无权限删除: {}", addressId);
            throw new BusinessException(ErrorCode.NULL_ERROR, "地址不存在或无权限");
        }

        // 使用 UpdateWrapper 强制更新 isDelete 字段
        UpdateWrapper<Address> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", addressId)
                .set("isDelete", DELETED);

        boolean deleted = this.update(updateWrapper);
        if (!deleted) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        log.info("地址 {} 已软删除", addressId);
    }

    @Override
    public AddressDTO getAddressById(Integer addressId, HttpServletRequest request) {
        AuthUtil.checkUserLogin(request);
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", addressId)
                .eq("isDelete", NOT_DELETED);
        Address add = this.getById(queryWrapper);
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(add, addressDTO);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getUserAddresses(HttpServletRequest request) {
        User curUser = AuthUtil.checkUserLogin(request);
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", curUser.getId())
                .eq("isDelete", NOT_DELETED)
                .orderByAsc("updateTime");
        List<Address> addList = this.list(queryWrapper);
        List<AddressDTO> voList = new ArrayList<>();
        for (Address add : addList) {
            AddressDTO dto = new AddressDTO();
            BeanUtils.copyProperties(add, dto);
            voList.add(dto);
        }
        return voList;
    }

    @Override
    public boolean setDefaultAddress(AddressDTO dto, HttpServletRequest request) {
        UpdateWrapper<Address> update = new UpdateWrapper<>();
        update.eq("id", dto.getId())
                .set("isDefault", dto.getIsDefault());
        boolean saved = this.update(update);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }
}