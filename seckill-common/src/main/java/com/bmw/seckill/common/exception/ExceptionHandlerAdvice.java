package com.bmw.seckill.common.exception;

import com.alibaba.fastjson.JSON;
import com.bmw.seckill.common.base.BaseResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * springMvc异常处理
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse baseException(BaseException exception) {
        return BaseResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidFormatException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse badRequestException(IllegalArgumentException exception) {
        log.warn("参数异常", exception);
        return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "参数错误");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse badRequestException(Exception exception) {
        log.error("系统异常", exception);
        return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "坏的请求");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse exception(Throwable throwable) {
        log.error("系统异常", throwable);
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器太忙碌了~让它休息一会吧!");

    }

    /**
     * 请求参数校验异常捕获
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse handleException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolationSet = exception.getConstraintViolations();
        if (!constraintViolationSet.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation violation : constraintViolationSet) {
                errors.add(violation.getMessage());
            }
            return BaseResponse.error(ErrorMessage.PARAM_ERROR.getCode(), JSON.toJSONString(errors));
        }
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器太忙碌了~让它休息一会吧!");
    }

    /**
     * 请求参数校验异常捕获
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse handleException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        return BaseResponse.error(ErrorMessage.PARAM_ERROR.getCode(), fieldError.getDefaultMessage());
    }

}
