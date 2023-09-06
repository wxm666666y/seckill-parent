package com.bmw.seckill.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.common.entity.CommonWebUser;
import com.bmw.seckill.common.exception.ErrorMessage;
import com.bmw.seckill.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 验证用户是否已经登录，需要验证的接口必须登录.
 */
@Slf4j
@WebFilter(filterName="userLoginFilter", urlPatterns = "/*")
public class UserLoginFilter implements Filter {

    @Autowired
    private RedisUtil redisUtil;

    //本filter配置的是拦截所有，urlPattern是配置的需要拦截的地址，其他地址不做拦截
    @Value("${auth.login.pattern}")
    private String urlPattern;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession();

        String url = request.getRequestURI();
        log.info("url:=" +url+ ",pattrn:="+urlPattern);
        if (url.matches(urlPattern)) {
            //可以先通过session来判断是否登录
            if (session.getAttribute(WebUserUtil.SESSION_WEBUSER_KEY) != null) {
                filterChain.doFilter(request, response);
                return;
            } else {
                //token我们此处约定保存在http协议的header中，也可以保存在cookie中，
                // 调用我们接口的前端或客户端也会保存cookie，具体使用方式由公司确定
                String tokenValue = request.getHeader("token");
                if (StringUtils.isNotEmpty(tokenValue)) { //判断token的值是否为空
                    Object object = redisUtil.get(tokenValue); //如果不为空,如何去redis中获取token的值
                    if (object != null) {
                        //因为token是存的那个序列化字符串,所以将token的值进行反序列化
                        CommonWebUser commonWebUser = JSONObject.parseObject(object.toString(), CommonWebUser.class);
                        //然后放到session中
                        session.setAttribute(WebUserUtil.SESSION_WEBUSER_KEY, commonWebUser);

                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        //返回接口调用方需要登录的错误码，接口调用方开始登录
                        returnJson(response);
                        return;
                    }
                } else {
                    //返回接口调用方需要登录的错误码，接口调用方开始登录
                    returnJson(response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
        return;
    }

    /**
     * 返回需要登录的约定格式的错误码，接口调用方根据错误码进行登录操作.
     */
    private void returnJson(ServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            BaseResponse baseResponse = new BaseResponse(ErrorMessage.USER_NEED_LOGIN.getCode(),
                    ErrorMessage.USER_NEED_LOGIN.getMessage(), null);
            writer.print(JSON.toJSONString(baseResponse));
        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
