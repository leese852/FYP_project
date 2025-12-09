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
    /**
     * dish id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 菜品名稱
     */
    private String dishName;

    /**
     * 價錢
     */
    private BigDecimal price;

    /**
     * 類別ID
     */
    private Integer categoryId;

    /**
     * 菜品介紹
     */
    private String description;

    /**
     * 圖片地址
     */
    private String imgUrl;

    /**
     * 0-禁用/下架 1-正常
     */
    private Integer isAvailable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新時間
     */
    private Date updateTime;

    /**
     * 是否删除 1-刪除
     */
    private Integer isDelete;
}