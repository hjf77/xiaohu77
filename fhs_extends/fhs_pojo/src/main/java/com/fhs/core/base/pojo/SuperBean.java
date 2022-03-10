package com.fhs.core.base.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的VO DO都需要继承此类
 *
 * @param <T>
 * @author user
 * @date 2020-05-19 11:51:55
 */
@Data
@Slf4j
@SuppressWarnings({"serial", "rawtypes"})
public class SuperBean<T extends SuperBean> extends BaseObject<T> {

    /**
     * 翻译map 给transervice用的
     */
    @TableField(exist = false)
    @JsonIgnore
    private Map<String, String> transMap = new HashMap<>();

    /**
     * 数据权限
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, String> dataPermissin = new HashMap<>();

    /**
     * 配合mybatis jpa between注解过滤条件使用
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, String> between = new HashMap<>();

    /**
     * 配合mybatis jpa in注解使用
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, String> inFilter = new HashMap<>();

    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Map<String, Object> userInfo = new HashMap<>();

    /**
     * 高级搜索过滤条件
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private String extAdvanceFilterParam;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Integer start;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @JsonIgnore
    private Integer PageSize;




}
