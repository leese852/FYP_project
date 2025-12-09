package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName dish
 */
@TableName(value ="dish")
@Data
public class Dish {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String dishName;
    private BigDecimal price;
    private Integer categoryId;
    private String description;
    private byte[] imgUrl;
    private Integer isAvailable;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}