package com.leese.usercenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class AddressDTO {
    private Integer id;
    private Integer userId;
    private String contactName;
    private String contactPhone;
    private String address;
    private Integer isDefault;
}
