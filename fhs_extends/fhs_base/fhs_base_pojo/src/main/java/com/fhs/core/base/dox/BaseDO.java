package com.fhs.core.base.dox;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.constant.Constant;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.pojo.SuperBean;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.mybatis.jpa.annotation.Between;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
public abstract class BaseDO<T extends BaseDO> extends SuperBean<T> implements VO {

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
    @Trans(type = TransType.AUTO_TRANS, key = "sysUser#createUser")
    protected String createUser;

    /**
     * 创建时间
     */
    @Between
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    protected Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("修改人")
    @TableField("update_user")
    @Trans(type = TransType.AUTO_TRANS, key = "sysUser#updateUser")
    protected String updateUser;

    /**
     * 更新时间
     */
    @Between
    @ApiModelProperty("修改时间")
    @TableField("update_time")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    protected Date updateTime;

    @TableLogic
    @TableField("is_delete")
    @JSONField(serialize = false)
    @ApiModelProperty("是否删除")
    protected Integer isDelete;


    /**
     * 插入之前调用
     */
    public void preInsert(String userId) {
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
    public void preUpdate(String userId) {
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
    public Object getPkey() {
        Field idField = getIdField(true);
        try {
            return idField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
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

    @JSONField(serialize=false)
    @JsonIgnore
    public BaseService getBaseService(){
        // 如果父类直接是basedo代表是个do如果父类不是basedo代表应是个vo
        Class clazz = this.getClass().getSuperclass() == BaseDO.class ? this.getClass() : this.getClass().getSuperclass();
        Type[] types  = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
        BaseService baseService = SpringContextUtil.getBeanByClass(BaseService.class,types[0].getTypeName(),1);
        return baseService;
    }

    /**
     * 插入
     * @return
     */
    public int insert(){
        return this.getBaseService().insertSelective((BaseDO) this);
    }

    /**
     * 以自己当做参数查询
     * @return
     */
    public  <V> List<V> findForList(){
        return this.getBaseService().findForList((BaseDO) this);
    }

    /**
     * 根据主键修改 不包含null
     * @return
     */
    public boolean updateSelectiveById(){
        return this.getBaseService().updateJpa((BaseDO) this);
    }

    /**
     * 根据主键修改 包含null
     * @return
     */
    public boolean updateByPkey(){
        return this.getBaseService().update((BaseDO) this);
    }


    /**
     * 把自己当做参数做删除
     * @return
     */
    public boolean deleteByPkey(){
        return this.getBaseService().delete((BaseDO) this);
    }

    /**
     * 查询总数
     * @return
     */
    public int findCount(){
        return this.getBaseService().findCount((BaseDO) this);
    }

    /**
     * 查询单个
     * @return
     */
    public <V> V findOne(){
        return (V) this.getBaseService().findBean((BaseDO) this);
    }

}
