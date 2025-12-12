package com.leese.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName address
 */
@TableName(value ="address")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class Address {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String contactName;
    private String contactPhone;
    private String address;
    private Integer isDefault;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}