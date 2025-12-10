package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName categories
 */
@TableName(value ="categories")
@Data
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