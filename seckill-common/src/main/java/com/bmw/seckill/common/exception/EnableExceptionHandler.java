package com.bmw.seckill.common.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zh
 * @ClassName: EnableExceptionHandler
 * @date 2021/2/5 16:28
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import({ExceptionHandlerAdvice.class})
public @interface EnableExceptionHandler {
}
