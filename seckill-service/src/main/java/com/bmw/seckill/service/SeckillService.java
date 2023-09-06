package com.bmw.seckill.service;

import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.model.http.SeckillReq;
import com.bmw.seckill.model.http.SeckillReqV2;
import com.bmw.seckill.model.http.SeckillReqV3;

public interface SeckillService {

    /**
     * 秒杀下单操作
     * @param req
     * @return
     */
    BaseResponse sOrder(SeckillReq req);

    /**
     * 悲观锁实现方式
     * @param req
     * @return
     */
    BaseResponse pOrder(SeckillReq req);

    /**
     * 乐观锁实现方式
     * @param req
     * @return
     */
    BaseResponse oOrder(SeckillReq req) throws Exception;

    /**
     * Redis+lua扣减库存(原子性)
     * @param req
     * @return
     * @throws Exception
     */
    BaseResponse cOrder(SeckillReq req) throws Exception;

    /**
     * 使用redis分布式锁实现(Redisson方案)
     * @param req
     * @return
     */
    BaseResponse redissonOrder(SeckillReq req);

    /**
     * 接口限流措施(令牌桶,分布式限流redis+lua脚本)
     * @param req
     * @return
     * @throws Exception
     */
    BaseResponse orderV1(SeckillReq req) throws Exception;

    /**
     * 单纯的下单方法
     * 用来测试Nginx+Lua构建的高性能秒杀设计
     * @param req
     * @return
     */
    BaseResponse createOrder(SeckillReq req);

    /**
     * 获取验证值
     * @param req
     * @return
     */
    BaseResponse<String> getVerifyHash(SeckillReq req);

    /**
     * 通过验证码来检测是否能够下单的下单接口
     * @param req
     * @return
     * @throws Exception
     */
    BaseResponse orderV2(SeckillReqV2 req) throws Exception;

    /**
     * 秒杀下单优化（图片验证码）
     * @param req
     * @return
     * @throws Exception
     */
    BaseResponse orderV3(SeckillReqV3 req) throws Exception;

    /**
     * 秒杀下单优化（单用户频次限制）
     */
    BaseResponse orderV4(SeckillReqV2 req) throws Exception;
}
