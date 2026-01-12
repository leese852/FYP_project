package com.leese.usercenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * 數據序列化
 */
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 114152300L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
