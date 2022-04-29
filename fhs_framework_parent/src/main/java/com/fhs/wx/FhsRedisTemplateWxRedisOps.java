package com.fhs.wx;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.common.util.locks.RedisTemplateSimpleDistributedLock;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.Lock;

public class FhsRedisTemplateWxRedisOps extends RedisTemplateWxRedisOps {

    private StringRedisTemplate fhsRedisTemplate;

    public FhsRedisTemplateWxRedisOps(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
        this.fhsRedisTemplate = redisTemplate;
    }

    @Override
    public Lock getLock(@NonNull String key) {
        return new FhsRedisTemplateSimpleDistributedLock(fhsRedisTemplate, key, 60 * 1000);
    }
}
class FhsRedisTemplateSimpleDistributedLock extends RedisTemplateSimpleDistributedLock{
    public FhsRedisTemplateSimpleDistributedLock(@NonNull StringRedisTemplate redisTemplate, int leaseMilliseconds) {
        super(redisTemplate, leaseMilliseconds);
    }

    public FhsRedisTemplateSimpleDistributedLock(@NonNull StringRedisTemplate redisTemplate, @NonNull String key, int leaseMilliseconds){
        super(redisTemplate,key,leaseMilliseconds);
    }

    @Override
    public boolean tryLock() {
        return true;
    }
}
