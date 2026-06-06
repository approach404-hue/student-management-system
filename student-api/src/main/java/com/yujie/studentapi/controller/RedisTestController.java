package com.yujie.studentapi.controller;

import com.yujie.studentapi.common.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class RedisTestController {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisTestController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/redis/test")
    public Result<String> testRedis() {
        stringRedisTemplate.opsForValue().set(
                "test:hello",
                "hello redis",
                Duration.ofMinutes(1)
        );

        String value = stringRedisTemplate.opsForValue().get("test:hello");

        return Result.success("Redis连接成功", value);
    }
}