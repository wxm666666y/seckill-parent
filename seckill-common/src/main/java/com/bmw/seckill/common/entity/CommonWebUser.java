package com.bmw.seckill.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础辅助类CommonWebUser, 这就是一个普通的把SeckillUser转换为CommonWebUser,用来规避隐私信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonWebUser implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private Date createTime;

    private Date updateTime;
}
