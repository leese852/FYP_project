package com.leese.usercenter.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RiderEntity {
    private Long employeeId;
    private String name;
    private String role; // 例如 "配送员"
    private String loginCredentials; // 可存储账号信息或引用外部表
    private Long restaurantId;
}
