package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName address
 */
@TableName(value ="address")
@Data
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