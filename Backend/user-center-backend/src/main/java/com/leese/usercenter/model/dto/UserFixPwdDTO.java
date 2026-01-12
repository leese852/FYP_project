package com.leese.usercenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class UserFixPwdDTO {
    private String newPwd;
    private String oldPwd;
}
