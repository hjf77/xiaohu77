package com.fhs.core.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.common.utils.StringUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的VO DO都需要继承此类
 *
 * @param <T>
 */
@SuppressWarnings({"serial", "rawtypes"})
@Data
public class SuperBean<T extends SuperBean> extends BaseObject<T> {

    /**
     * 子类id字段缓存
     */
    @TableField(exist = false)
    @JsonIgnore
    @JSONField(serialize = false,deserialize = false)
    private static final Map<Class<?>, Field> ID_FIELD_CACHE_MAP = new HashMap<>();


    /**
     * 翻译map 给transervice用的
     */
    @TableField(exist = false)
    @JSONField(serialize = false,deserialize = false)
    private Map<String, String> transMap = new HashMap<>();

    /**
     * 数据权限
     */
    @TableField(exist = false)
    @JSONField(serialize = false,deserialize = false)
    private Map<String, String> dataPermissin = new HashMap<>();

    /**
     * 配合mybatis jpa between注解过滤条件使用
     */
    @TableField(exist = false)
    @JSONField(serialize = false,deserialize = false)
    private Map<String, String> between = new HashMap<>();

    /**
     * 配合mybatis jpa in注解使用
     */
    @TableField(exist = false)
    private Map<String, String> inFilter = new HashMap<>();

    /**
     * 将一组过滤条件添加到in中
     *
     * @param field   字段名字
     * @param inParam 参数集合
     */
    public void add2In(String field, List<String> inParam) {
        inFilter.put(field, StringUtil.getStrToIn(inParam));
    }



    /**
     * 获取主键
     *
     * @return 主键
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public Object getPubPkey() {
        Field idField = getIdField(true);
        if (idField == null) {
            return null;
        }
        try {
            return idField.get(this);
        } catch (IllegalAccessException e) {
            return null;
        }
    }


    /**
     * 获取子类id字段
     *
     * @return 子类id字段
     */
    @JsonIgnore
    @JSONField(serialize = false)
    protected Field getIdField(boolean isThrowError) {
        if (ID_FIELD_CACHE_MAP.containsKey(this.getClass())) {
            return ID_FIELD_CACHE_MAP.get(this.getClass());
        }
        List<Field> fieldList = ReflectUtils.getAnnotationField(this.getClass(), javax.persistence.Id.class);
        if (fieldList != null && fieldList.size() == 0) {
            fieldList = ReflectUtils.getAnnotationField(this.getClass(), TableId.class);
            if (fieldList != null && fieldList.size() == 0) {
                Field idField = ReflectUtils.getDeclaredField(this.getClass(), "id");
                fieldList = Arrays.asList(idField);
                if (idField == null && isThrowError) {
                    throw new RuntimeException("找不到" + this.getClass() + "的id注解");
                }
            }
        }
        if (fieldList == null || fieldList.get(0) == null) {
            return null;
        }
        fieldList.get(0).setAccessible(true);
        ID_FIELD_CACHE_MAP.put(this.getClass(), fieldList.get(0));
        return fieldList.get(0);
    }
}
