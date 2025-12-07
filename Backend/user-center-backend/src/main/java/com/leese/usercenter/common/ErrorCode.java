package com.leese.usercenter.common;

/**
 * 錯誤碼
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAM_ERROR(40000, "請求參數錯誤", ""),
    NOT_LOGIN(40100, "未登錄", ""),
    NO_AUTH(40101, "無權限", ""),
    NULL_ERROR(40400, "請求數據爲空", ""),
    SYSTEM_ERROR(50000, "系統内部異常", "");


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
