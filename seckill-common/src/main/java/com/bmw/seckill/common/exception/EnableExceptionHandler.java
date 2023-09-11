package com.bmw.seckill.common.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import({ExceptionHandlerAdvice.class})
public @interface EnableExceptionHandler {
}
