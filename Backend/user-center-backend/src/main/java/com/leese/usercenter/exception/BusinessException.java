package com.leese.usercenter.exception;

import com.leese.usercenter.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author leese
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public BusinessException(ErrorCode errorCode) {
        //將message傳遞到RuntimeException父類
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    public BusinessException(String message,int code ,String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

}
