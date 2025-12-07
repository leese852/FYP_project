package com.leese.usercenter.model.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;

@Data
//Serializable 主要用途是标识一个类的实例可以被序列化，
// 即该对象的状态可以通过某种方式保存到存储介质中（如硬盘、数据库或通过网络传输），
// 并在之后能够从这些介质中恢复对象的状态。

public class userLoginRequest implements Serializable {
    private String userAccount;

    private String userPassword;

}
