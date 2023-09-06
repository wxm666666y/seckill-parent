package com.bmw.seckill.service;

import com.bmw.seckill.security.AdminUser;
import com.bmw.seckill.security.Token;

public interface AdminTokenService {
    AdminUser getLoginUser(String token);
    Token saveToken(AdminUser loginUser);
    void deleteToken(String token);
    void refresh(AdminUser loginUser);

}