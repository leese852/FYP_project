package com.leese.usercenter.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
public class RiderEntity {
    private Long employeeId;          // 騎手ID
    private String name;              // 騎手姓名
    private String role;              // 角色，例如 "配送員"
    private String loginCredentials;  // 登入憑證或帳號資訊
    private Long restaurantId;        // 所屬餐廳ID
    private String phone;             // 騎手電話

    // ✅ 新增欄位，對應資料表
    private Timestamp createTime;     // 創建時間
    private Timestamp updateTime;     // 更新時間
    private Integer isDelete;         // 是否刪除 (0未刪除 1刪除)
}
