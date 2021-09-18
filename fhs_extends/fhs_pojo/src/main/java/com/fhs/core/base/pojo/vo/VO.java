package com.fhs.core.base.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.common.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持vo 即可进行翻译
 *
 * @author user
 * @date 2020-05-19 11:51:08
 */
public interface VO {
    /**
     * 获取翻译map
     *
     * @return 翻译map
     */
    Map<String, String> getTransMap();

    /**
     * 获取主键
     *
     * @return 主键
     */
    @JSONField(serialize = false, deserialize = false)
    Object getPkey();


    /**
     * 是否软删除
     *
     * @return 0 为软删除 1 已经软删除
     */
    Integer getIsDelete();
}
