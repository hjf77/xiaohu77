package com.fhs.redis.service.impl;

import com.fhs.redis.mapper.RedisDao;
import com.fhs.redis.service.ZSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZSetServiceImpl implements ZSetService {

    @Autowired
    private RedisDao redisDao;

    @Override
    public void updateZSetValue(String serverName, int dbIndex, String key, double score, String member) {
        redisDao.updateZSetValue(serverName, dbIndex, key, score, member);
    }

    @Override
    public void delZSetValue(String serverName, int dbIndex, String key, String member) {
        redisDao.delZSetValue(serverName, dbIndex, key, member);
    }

}
