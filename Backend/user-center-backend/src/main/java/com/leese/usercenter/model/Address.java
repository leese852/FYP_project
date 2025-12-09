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
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用戶id
     */
    private Integer userId;

    /**
     * 收貨人
     */
    private String contactName;

    /**
     * 聯係電話
     */
    private String contactPhone;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否默認 1=default
     */
    private Integer isDefault;

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