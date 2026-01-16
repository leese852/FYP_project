package com.leese.usercenter.controller.user;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.model.dto.AddressDTO;
import com.leese.usercenter.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController()
@RequestMapping("user/address")
@Slf4j
public class AddressController {
    @Autowired
    AddressService addressService;

    @PostMapping("/add")
    public BaseResponse<String> addAddress(@RequestBody AddressDTO dto, HttpServletRequest request){
        log.info("要添加的地址信息：{}",dto);
        addressService.addAddress(dto,request);
        return ResultUtils.success();
    }

    @PutMapping("/update")
    public BaseResponse<String> updateAddress(@RequestBody AddressDTO dto, HttpServletRequest request){
        log.info("更新地址信息：{}",dto);
        addressService.updateAddress(dto,request);
        return ResultUtils.success();
    }

    @DeleteMapping()
    public BaseResponse<String> deleteAddress(@RequestParam Integer addressId, HttpServletRequest request){
        log.info("删除的地址id：{}",addressId);
        addressService.deleteAddress(addressId,request);
        return ResultUtils.success();
    }

    @GetMapping("/id")
    public BaseResponse<AddressDTO> getById(@RequestParam Integer addressId, HttpServletRequest request){
        log.info("地址id：{}",addressId);
        AddressDTO dto = addressService.getAddressById(addressId,request);
        return ResultUtils.success(dto);
    }

    @GetMapping("/list")
    public BaseResponse<List<AddressDTO>> getList(HttpServletRequest request){
        List<AddressDTO>addList = addressService.getUserAddresses(request);
        return ResultUtils.success(addList);
    }
}
