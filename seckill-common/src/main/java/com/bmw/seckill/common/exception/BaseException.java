package com.bmw.seckill.common.exception;

import com.bmw.seckill.common.base.BaseResponse;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BaseException extends RuntimeException implements Serializable {
    public static final long serialVersionUID = -1822114685844044268L;

    private Integer code;
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static BaseException error(ErrorMessage message) {
        return new BaseException(message.getCode(), message.getMessage());
    }

    public static BaseException error(BaseResponse baseResponse) {
        return new BaseException(baseResponse.getCode(), baseResponse.getMessage());
    }

    public void setMessage(String defaultMessage) {
        this.message = defaultMessage;
    }
}
