package com.leese.usercenter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用戶昵稱
     */
    private String username;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用戶賬號
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别 0-男 1-女
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 電話
     */
    private String tel;

    /**
     * 狀態 0-正常 1-禁用
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date creaetTime;

    /**
     * 更新時間
     */
    private Date updateTime;

    /**
     * 是否删除
     */
//    @TableLogic(value = "0", delval = "1")
//    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 星球編號
     */
    private String planteCode;
}