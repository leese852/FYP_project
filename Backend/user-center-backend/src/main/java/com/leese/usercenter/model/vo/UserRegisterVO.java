package com.leese.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * 數據序列化
 */
@Data
public class UserRegisterVO implements Serializable {
//    private static final long serialVersionUID = 114152300L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
