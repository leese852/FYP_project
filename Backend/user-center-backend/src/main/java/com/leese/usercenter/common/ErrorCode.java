package com.leese.usercenter.common;

/**
 * 錯誤碼
 */
public enum ErrorCode {
    // 基础错误
    SUCCESS(0,"ok",""),
    PARAM_ERROR(40000, "請求參數錯誤", ""),
    NOT_LOGIN(40100, "未登錄", ""),
    NO_AUTH(40101, "無權限", ""),
    NULL_ERROR(40400, "請求資源不存在", ""),
    SYSTEM_ERROR(50000, "系統内部異常", ""),

    // 业务错误 - 用户
    USER_NOT_FOUND(10001, "用户不存在", ""),
    USER_EXISTS(10002, "用户已存在", ""),
    USER_PASSWORD_ERROR(10003, "賬號和密碼錯誤", ""),

    // 业务错误 - 菜品
    DISH_NOT_FOUND(20001, "菜品不存在", ""),
    DISH_STATUS_ERROR(20002, "菜品狀態錯誤", ""),
    DISH_CATEGORY_NOT_FOUND(20003, "菜品分類不存在", ""),

    // 业务错误 - 订单
    ORDER_NOT_FOUND(30001, "订单不存在", ""),
    ORDER_STATUS_ERROR(30002, "订单狀態錯誤", ""),
    ORDER_CANCEL_FAILED(30003, "订单取消失敗", "");

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
