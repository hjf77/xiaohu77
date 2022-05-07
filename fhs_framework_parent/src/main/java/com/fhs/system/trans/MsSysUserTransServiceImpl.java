package com.fhs.system.trans;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.bean.SuperBean;
import com.fhs.core.trans.ITransTypeService;
import com.fhs.core.trans.TransService;
import com.fhs.redis.service.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 后台用户翻译实现
 *
 * @Filename: MsSysUserTransServiceImpl.java
 * @Description:
 * @Version: 1.0
 * @Author: qixiaobo
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Service
public class MsSysUserTransServiceImpl implements ITransTypeService, InitializingBean {

    /**
     * redis 缓存服务
     */
    @Autowired
    private RedisCacheService<String> redisCacheService;

    /**
     * jetcache
     */
    @CreateCache(cacheType = CacheType.BOTH,localExpire = 300,name = "ucenter:sysuser:")
    Cache<String,String> cacheUserName;


    /**
     * 注册自身为一个服务
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        TransService.registerTransType(Constant.USER_INFO, this);
    }

    /**
     * 翻译单条数据
     *
     * @param obj         需要翻译的对象
     * @param toTransList 需要翻译的字段
     */
    @Override
    public void transOne(SuperBean<?> obj, List<Field> toTransList) {
        for (Field tempField : toTransList) {
            tempField.setAccessible(true);
            String userId = null;
            try {
                userId = StringUtil.toString(tempField.get(obj));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!userId.contains(",")) {
                if (StringUtils.isEmpty(userId)) {
                    return;
                }
                obj.getTransMap().put(tempField.getName() + "UserName", cacheUserName.get(userId));
            } else {
                String[] userIds = userId.split(",");
                StringBuilder nameBuilder = new StringBuilder();
                for (String tempUserId : userIds) {
                    if (StringUtils.isEmpty(tempUserId)) {
                        continue;
                    }
                    if (redisCacheService.exists("ucenter:sysuser:" + tempUserId)) {
                        nameBuilder.append(cacheUserName.get(tempUserId) + ",");
                    }
                }
                obj.getTransMap().put(tempField.getName() + "UserName", nameBuilder.toString());
            }

        }

    }

    /**
     * 翻译多条数据
     *
     * @param objList     需要翻译的对象集合
     * @param toTransList 需要翻译的字段集合
     */
    @Override
    public void transMore(List<? extends SuperBean<?>> objList, List<Field> toTransList) {
        for (SuperBean<?> obj : objList) {
            transOne(obj, toTransList);
        }
    }

}
