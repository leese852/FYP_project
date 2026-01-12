package com.leese.usercenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
//Serializable 主要用途是标识一个类的实例可以被序列化，
// 即该对象的状态可以通过某种方式保存到存储介质中（如硬盘、数据库或通过网络传输），
// 并在之后能够从这些介质中恢复对象的状态。

public class UserLoginDTO implements Serializable {
    private String userAccount;

    private String userPassword;

}
