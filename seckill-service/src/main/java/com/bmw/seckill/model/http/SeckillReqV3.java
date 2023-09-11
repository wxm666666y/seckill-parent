package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SeckillReqV3 implements Serializable {

    @NotNull(message = "产品id 不能为空")
    private Long productId;

    private Long userId;
    @NotEmpty(message = "图片验证码标识 不能为空")
    private String imageId;
    @NotEmpty(message = "图片验证码 不能为空")
    private String imageCode;
}
