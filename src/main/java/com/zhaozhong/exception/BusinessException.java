package com.zhaozhong.exception;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.zhaozhong.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -3397778147177619890L;
    private final int code;
    private final String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(BaseResponse errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    public BusinessException(BaseResponse errorCode,String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
