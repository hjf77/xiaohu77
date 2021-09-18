package com.fhs.redis.service.impl;

import com.fhs.redis.mapper.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhs.redis.service.ListService;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private RedisDao redisDao;

    @Override
    public void updateListValue(String serverName, int dbIndex, String key, String value) {
        redisDao.lupdateListValue(serverName, dbIndex, key, value);
    }

    @Override
    public void delListValue(String serverName, int dbIndex, String key) {
        redisDao.ldelListValue(serverName, dbIndex, key);
    }

}
