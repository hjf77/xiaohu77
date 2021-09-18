package com.fhs.redis.service;

public interface HashService {

    void delHashField(String serverName, int dbIndex, String key, String field);

    void updateHashField(String serverName, int dbIndex, String key, String field, String value);

}
