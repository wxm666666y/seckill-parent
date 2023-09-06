package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 中北大学软件学院王袭明版权声明(c) 2023/8/3
 */
@Data
public class SeckillReq {

    @NotNull(message = "产品id 不能为空")
    private Long productId;

    private Long userId;
}
