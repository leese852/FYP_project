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

    /**
     * 類別名稱
     */
    private String categoryName;

    /**
     * 顯示權重
     */
    private Integer displayOrder;

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