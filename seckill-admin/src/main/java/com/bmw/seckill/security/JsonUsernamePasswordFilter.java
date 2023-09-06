package com.bmw.seckill.security;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmw.seckill.common.exception.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类可以继承自UsernamePasswordAuthenticationFilter，可以设置这个filter登录成功，
 * 登录失败以及在哪个url起作用，这块就不需要写专门的登录方法
 */
public class JsonUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        //attempt Authentication when Content-Type is json
        if (request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            //use jackson to deserialize json
            UsernamePasswordAuthenticationToken authRequest;
            try (InputStream is = request.getInputStream()) {
                String body = IoUtil.read(is, CharsetUtil.CHARSET_UTF_8);
                JSONObject jsonObject = JSON.parseObject(body);//解析读取到的字符串为 JSON 对象，使用的是阿里巴巴的 JSON.parseObject(body) 方法。假设请求的 JSON 数据为 { "data": { "username": "user123", "password": "pass123" } }，则解析后的 jsonObject 为一个包含 "data" 对象的 JSON 对象。
                //从 jsonObject 中获取 "data" 对象，并进一步从 "data" 对象中获取 "username" 和 "password" 字段的值。
                String username = jsonObject.getJSONObject("data").getString("username");
                String password = jsonObject.getJSONObject("data").getString("password");

                // 校验IP地域 start
//                String requestsIp = IPUtil.getIpAddress(request);
//                if (IpCheckUtil.checkIp(ipTableService.getList(), username, requestsIp) == false) {
//                    ErrorMessage error = ErrorMessage.UN_USERNAME_STATUS;
//                    error.setMessage("登录账号ip区域不允许, 您的IP："+requestsIp);
//                    throw BaseAuthenticationException.error(error);
//                }
                // 校验IP地域 end

                authRequest = new UsernamePasswordAuthenticationToken(username, password);
                //使用 setDetails(request, authRequest) 方法为 authRequest 设置认证的详细信息，这里包括设置请求的 HttpServletRequest。
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
                throw BaseAuthenticationException.error(ErrorMessage.LOGIN_ERROR);

            }
        } else {
            throw BaseAuthenticationException.error(ErrorMessage.NO_JSON);
        }

    }


}
