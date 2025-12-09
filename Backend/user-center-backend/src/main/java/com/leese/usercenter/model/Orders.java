package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 訂單編號
     */
    private String orderId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 1-待处理 2-准备中 3-配送中 4-已送达 5-已取消
     */
    private Integer status;

    /**
     * 縂價錢
     */
    private BigDecimal totalAmount;

    /**
     * 支付方法
     */
    private String paymentMethod;

    /**
     * 備注
     */
    private String remark;

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