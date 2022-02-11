package com.fhs.core.base.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.pojo.SuperBean;
import com.fhs.core.base.vo.QueryField;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import com.fhs.excel.anno.IgnoreExport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 所有的do请都继承此do
 *
 * @Filename: BaseDO.java
 * @Description:
 * @Version: 1.0
 * @Author: jackwang
 * @Email: wanglei@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
@SuppressWarnings("rawtypes")
@Data
public abstract class BasePO<T extends BasePO> extends SuperBean<T> implements VO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * 子类id字段缓存
     */
    @TableField(exist = false)
    @JsonIgnore
    private static final Map<Class<?>, Field> ID_FIELD_CACHE_MAP = new HashMap<>();

    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty("创建人")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.basics.po.UcenterMsUserPO", alias = "createUser",fields = "userName",serviceName = "basic",dataSource = "basic")
    protected Long createUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    protected Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("修改人")
    @TableField("update_user")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.basics.po.UcenterMsUserPO", alias = "updateUser",fields = "userName",serviceName = "basic",dataSource = "basic")
    protected Long updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty("修改时间")
    @TableField("update_time")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    protected Date updateTime;

    @TableLogic
    @IgnoreExport
    @JsonIgnore
    @TableField("is_delete")
    @JSONField(serialize = false)
    @ApiModelProperty("是否删除")
    protected Integer isDelete;


    /**
     * 插入之前调用
     */
    public void preInsert(Long userId) {
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
        this.createUser = userId;
        this.updateUser = userId;
        this.isDelete = Constant.INT_FALSE;
    }

    /**
     * 更新之前调用
     */
    public void preUpdate(Long userId) {
        this.updateTime = new Date();
        this.updateUser = userId;
    }


    /**
     * 获取主键
     *
     * @return 主键
     */
    @Override
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public Serializable getPkey() {
        Field idField = getIdField(true);
        try {
            return (Serializable)idField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取主键
     *
     * @return 主键
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public void setPkey(Object pkey) {
        Field idField = getIdField(true);
        try {
            idField.set(this, pkey);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取子类id字段
     *
     * @return 子类id字段
     */
    public Field getIdField(boolean isThrowError) {
        if (ID_FIELD_CACHE_MAP.containsKey(this.getClass())) {
            return ID_FIELD_CACHE_MAP.get(this.getClass());
        }
        List<Field> fieldList = ReflectUtils.getAnnotationField(this.getClass(), javax.persistence.Id.class);
        if (fieldList.size() == 0) {
            fieldList = ReflectUtils.getAnnotationField(this.getClass(), TableId.class);
            if (fieldList.size() == 0) {
                if (isThrowError) {
                    throw new RuntimeException("找不到" + this.getClass() + "的id注解");
                }
            }
            fieldList.get(0).setAccessible(true);
            return fieldList.get(0);
        }
        fieldList.get(0).setAccessible(true);
        ID_FIELD_CACHE_MAP.put(this.getClass(), fieldList.get(0));
        return fieldList.get(0);
    }

    /**
     * 把本身不为null的属性转换为wrapper
     * @return
     */
    public QueryWrapper<T> asWrapper(){
        List<Field>  fields = ReflectUtils.getAnnotationField(this.getClass(),TableField.class);
        //过滤掉忽略字段
        fields = fields.stream().filter(field -> {
            return field.getAnnotation(TableField.class).exist();
        }).collect(Collectors.toList());
        List<Field>  idFields = ReflectUtils.getAnnotationField(this.getClass(),TableId.class);
        if(!idFields.isEmpty()){
            fields.addAll(idFields);
        }
        QueryFilter<T> filter = new QueryFilter<>();
        Object value = null;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                value = field.get(this);
                //不等于null不等于空的时候加入到条件里去
                if(value!=null && !"".equals(value)){
                    filter.getQuerys().add(QueryField.builder().property(field.getName()).value(value).operation("=").group("main").relation("AND").build());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return filter.asWrapper(this.getClass());
    }

}
