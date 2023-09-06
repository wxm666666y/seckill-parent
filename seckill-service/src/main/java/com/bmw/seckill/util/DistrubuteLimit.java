package com.bmw.seckill.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式限流工具
 * 中北大学软件学院王袭明版权声明(c) 2023/8/10
 */
@Component
public class DistrubuteLimit {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<Long> getRedisScript;

    private static final String LIMIT_LUA = "local times = redis.call('incr', KEYS[1])\n" +
            "if times == 1 then\n" +
            "    redis.call('expire', KEYS[1], ARGV[1])\n" +
            "end\n" +
            "if times > tonumber(ARGV[2]) then\n" +
            "    return 0\n" +
            "end\n" +
            "return 1";

    @PostConstruct
    public void init(){
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptText(LIMIT_LUA);
    }

    public Boolean exec() throws IOException {
        List<String> keyList = new ArrayList();
        String key = "ip:" + System.currentTimeMillis() / 1000; //此处将当前时间戳取秒数
        //String key = "ip:" + 1; //此处硬编码时间，保证请求都是在同一秒内发起
        keyList.add(key);
        return redisTemplate.execute(getRedisScript, keyList, 3000, 6) == 1 ? true : false;
    }
}
