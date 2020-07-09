package com.fhs.core.base.activerecord;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.base.service.BaseService;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 实现此接口即可实现activerecord
 * 必须拥有对应的baseservice实现类和实现此接口的类必须是BaseDO的子类
 * @author jackwang
 */
public interface ActiveRecordAble<D extends BaseDO<D>> {

    @JSONField(serialize=false)
    default BaseService getBaseService(){
        Type[] types  = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        BaseService baseService = SpringContextUtil.getBeanByClass(BaseService.class,types[0].getTypeName(),1);
        return baseService;
    }

    /**
     * 插入
     * @return
     */
    default int insert(){
        return this.getBaseService().insertSelective((BaseDO) this);
    }

    /**
     * 以自己当做参数查询
     * @return
     */
    default  <V> List<V> findForList(){
        return this.getBaseService().findForList((BaseDO) this);
    }

    /**
     * 根据主键修改 不包含null
     * @return
     */
    default boolean updateSelectiveById(){
        return this.getBaseService().updateJpa((BaseDO) this);
    }

    /**
     * 根据主键修改 包含null
     * @return
     */
    default boolean updateByPkey(){
        return this.getBaseService().update((BaseDO) this);
    }


    /**
     * 把自己当做参数做删除
     * @return
     */
    default boolean deleteByPkey(){
        return this.getBaseService().delete((BaseDO) this);
    }

    /**
     * 查询总数
     * @return
     */
    default int findCount(){
        return this.getBaseService().findCount((BaseDO) this);
    }

    /**
     * 查询单个
     * @return
     */
    default <V> V findOne(){
        return (V) this.getBaseService().findBean((BaseDO) this);
    }
}
