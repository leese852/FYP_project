package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName employee
 */
@TableName(value ="employee")
@Data
public class Employee {
    private Integer id;

    private String name;

    private String account;

    private String password;

    private String phone;

    private Integer age;

    private Integer gender;

    private Integer status;

    private Integer userRole;

    private Integer create_user;

    private Integer update_user;

    private Date create_time;

    private Date update_time;

    private byte[] pic;
}