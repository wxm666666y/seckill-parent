package com.bmw.seckill.controller;

import cn.hutool.core.util.IdUtil;
import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.common.base.Constant;
import com.bmw.seckill.model.vo.ImageCodeModel;
import com.bmw.seckill.util.RedisUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class VerifyCodeController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisUtil redisUtil;

//    @GetMapping("/seckill/imageCode")
//    public void getImageNumCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
//        // 创建字节数组用户存放图片信息
//        byte[] numCodeImgByte = null;
//        // 获得二进制输出流
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // 通过DefaultKaptcha获得随机验证码
//        String code = defaultKaptcha.createText();
//        // 将验证码存放在session中
//        request.getSession().setAttribute("code", code);
//        // 生成图片
//        BufferedImage image = defaultKaptcha.createImage(code);
//        // 将图片写入到流中
//        ImageIO.write(image, "jpg", baos);
//        numCodeImgByte = baos.toByteArray();
//        // 通过response设定响应请求类型
//        // no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用     缓存。
//        response.setHeader("Cache-Control", "no-store");
//        // no-cache指示请求或响应消息不能缓存
//        response.setHeader("Pragma", "no-cache");
//        /* expires是response的一个属性,它可以设置页面在浏览器的缓存里保存的时间 ,超过设定的时        间后就过期 。过期后再次
//         * 浏览该页面就需要重新请求服务器发送页面数据，如果在规定的时间内再次访问次页面 就不需从服务器传送 直接从缓存中读取。
//         * */
//        response.setDateHeader("Expires", 0);
//        // servlet接受request请求后接受图片形式的响应
//        response.setContentType("image/jpeg");
//        // 通过response获得输出流
//        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.write(numCodeImgByte);
//        outputStream.close();
//    }


    @RequestMapping("/seckill/code")
    @ResponseBody
    public BaseResponse<ImageCodeModel> getNumCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String imageId = IdUtil.objectId();
        // 获得二进制输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 通过DefaultKaptcha获得随机验证码
        String code = defaultKaptcha.createText();

        BufferedImage image = defaultKaptcha.createImage(code);
        // 将图片写入到流中
        ImageIO.write(image, "jpg", baos);

        ImageCodeModel vo = new ImageCodeModel();
        vo.setImageId(imageId);
        vo.setImageStr(Base64.encodeBase64String(baos.toByteArray()));
        redisUtil.set(String.format(Constant.redisKey.SECKILL_IMAGE_CODE, imageId), code, 60);
        return BaseResponse.OK(vo);
    }


    @RequestMapping("/code/page")
    public String index() {
        return "code";
    }

}
