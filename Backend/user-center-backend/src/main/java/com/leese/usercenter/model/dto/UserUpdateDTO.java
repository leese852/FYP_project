package com.leese.usercenter.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class UserUpdateDTO implements Serializable{
    
    private String username;
    private String avatar;
    private Byte gender;
    private String email;
    private String tel;
}

