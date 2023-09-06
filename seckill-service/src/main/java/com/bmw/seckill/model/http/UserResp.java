package com.bmw.seckill.model.http;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回的响应类
 */
@Data
public class UserResp implements Serializable {

    @Data
    public static class BaseUserResp implements  Serializable {

        private String token;
    }

}
