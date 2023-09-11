package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SeckillReqV2 implements Serializable {

    @NotNull(message = "产品id 不能为空")
    private Long productId;

    private Long userId;
    @NotBlank(message = "验证码 不能为空")
    private String verifyCode;
}
