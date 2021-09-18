package com.fhs.redis.service;

public interface SetService {

    void delSetValue(String serverName, int dbIndex, String key, String value);

    void updateSetValue(String serverName, int dbIndex, String key, String value);

}
