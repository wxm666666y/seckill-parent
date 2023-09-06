package com.bmw.seckill.service;

import com.bmw.seckill.model.SeckillAdmin;

import java.util.List;

public interface IAdminService {

    /**
     * 展示全部的admin用户信息.
     */
    List<SeckillAdmin> listAdmin();

    /**
     * 根据用户名获取后台用户信息.
     */
    SeckillAdmin findByUsername(String username);
}
