package com.bmw.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.bmw.seckill.common.base.BaseRequest;
import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.common.entity.CommonWebUser;
import com.bmw.seckill.common.exception.ErrorMessage;
import com.bmw.seckill.model.SeckillUser;
import com.bmw.seckill.model.http.UserReq;
import com.bmw.seckill.model.http.UserResp;
import com.bmw.seckill.security.WebUserUtil;
import com.bmw.seckill.service.UserService;
import com.bmw.seckill.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    //用来标识这是验证码
    private final String USER_PHONE_CODE_BEFORE = "u:p:c:b:";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 模拟发送短信验证码
     * @param req 包含手机号的请求体内容
     * @return  返回规定json格式response对象
     */
    @PostMapping("/getPhoneSmsCode")
    public BaseResponse<Boolean> getPhoneSmsCode(@Valid @RequestBody BaseRequest<UserReq.BaseUserInfo> req) {
        String phone = req.getData().getPhone();
        SeckillUser seckillUser = userService.findByPhone(phone);
        //先判断用户存在
        //接下来是调用第三方http接口发送短信验证码，通过验证码存储在redis中，方便后续判断，此处不展示http接口调用了
        if (seckillUser != null) {
            //短信验证码
            String randomCode = "123456";
            redisUtil.set(USER_PHONE_CODE_BEFORE + phone, randomCode,60*5);
            return BaseResponse.ok(true);
        } else return BaseResponse.ok(false);
    }

    /**
     * 进行验证码登录
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/userPhoneLogin")
    public BaseResponse userPhoneLogin(@Valid @RequestBody BaseRequest<UserReq.LoginUserInfo> req) throws Exception{
        UserReq.LoginUserInfo loginInfo = req.getData();

        //获取redis中该手机号的验证码
        Object existObj = redisUtil.get(USER_PHONE_CODE_BEFORE + loginInfo.getPhone());
        //如果该手机号的验证码不存在或者验证码不匹配
        //则返回验证码错误的response信息
        if (existObj == null || !existObj.toString().equals(loginInfo.getSmsCode())) {
            return BaseResponse.error(ErrorMessage.SMSCODE_ERROR);
        }
        else { //如果存在且匹配的话
            redisUtil.del(USER_PHONE_CODE_BEFORE + loginInfo.getPhone()); //则将该验证码从redis中删除

            SeckillUser seckillUser = userService.findByPhone(loginInfo.getPhone());
            //基础辅助类CommonWebUser, 这就是一个普通的把SeckillUser转换为CommonWebUser,用来规避隐私信息
            CommonWebUser commonWebUser = new CommonWebUser();
            BeanUtils.copyProperties(commonWebUser, seckillUser); //将secjillUser中对应的信息复制到commonWebUser中
            String token = UUID.randomUUID().toString().replaceAll("-","");
            //设置token超时时间为1个月，实际根据需求确定
            //token用作用户身份认证和授权的一种令牌
            redisUtil.set(token, JSON.toJSONString(commonWebUser), 60*60*24*30);
            UserResp.BaseUserResp resp = new UserResp.BaseUserResp();
            resp.setToken(token);
            return BaseResponse.ok(resp);
        }

    }

    //单纯测试用
    @GetMapping("/checkUserToken")
    public void checkUserToken() {
        CommonWebUser commonWebUser = WebUserUtil.getLoginUser();
        log.info(JSON.toJSONString(commonWebUser));
    }
}
