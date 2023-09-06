package com.bmw.seckill.security;

import com.alibaba.fastjson.JSONObject;
import com.bmw.seckill.common.entity.CommonWebUser;
import com.bmw.seckill.util.RedisUtil;
import com.bmw.seckill.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 因为WebUserUtil是普通类，不被spring管理，不能使用Autowired注入
 */
public class WebUserUtil {

    public static final String SESSION_WEBUSER_KEY = "web_user_key";

    /**
     * 获取当前用户
     *
     * @return
     */
    public static CommonWebUser getLoginUser() {

        // 获取相关对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpSession session = request.getSession();

        CommonWebUser commonWebUser = null;

        //先从session中获取,如果session中没有,则从redis中获取
        if (session.getAttribute(SESSION_WEBUSER_KEY) != null) {
            commonWebUser = (CommonWebUser)session.getAttribute(SESSION_WEBUSER_KEY);
        } else {
            //@Autowire
            //RedisUtil redisUtil作用是一样的,也是获取ioc管理的bean对象
            RedisUtil redisUtil = SpringContextHolder.getBean("redisUtil");
            if (StringUtils.isNotEmpty(request.getHeader("token"))) {
                Object object = redisUtil.get(request.getHeader("token"));
                if (object != null) {
                    commonWebUser = JSONObject.parseObject(object.toString(), CommonWebUser.class);
                    session.setAttribute(SESSION_WEBUSER_KEY, commonWebUser);
                }
            }
        }

        return commonWebUser;
    }


}
