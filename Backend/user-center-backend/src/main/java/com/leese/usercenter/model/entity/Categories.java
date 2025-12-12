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
 * @TableName categories
 */
@TableName(value ="categories")
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
public class Categories {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String categoryName;
    private Integer displayOrder;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}