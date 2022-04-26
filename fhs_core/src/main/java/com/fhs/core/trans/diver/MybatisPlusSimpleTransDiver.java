package com.fhs.core.trans.diver;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fhs.core.base.bean.SuperBean;
import com.fhs.core.trans.impl.SimpleTransService;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis plus 简单翻译驱动
 */
@Component
public class MybatisPlusSimpleTransDiver implements SimpleTransService.SimpleTransDiver {

    @Override
    public List<? extends SuperBean> findByIds(List<? extends Serializable> ids, Class<? extends SuperBean> targetClass) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("coll", ids);
        SqlSession sqlSession = this.sqlSession(targetClass);
        try {
            return sqlSession.selectList(this.sqlStatement(SqlMethod.SELECT_BATCH_BY_IDS, targetClass), map);
        } finally {
            this.closeSqlSession(sqlSession, targetClass);
        }
    }

    @Override
    public SuperBean findById(Serializable id, Class<? extends SuperBean> targetClass) {
        SqlSession sqlSession = this.sqlSession(targetClass);
        SuperBean result;
        try {
            result = (SuperBean) sqlSession.selectOne(this.sqlStatement(SqlMethod.SELECT_BY_ID, targetClass), id);
        } finally {
            this.closeSqlSession(sqlSession, targetClass);
        }
        return result;
    }

    protected SqlSession sqlSession(Class<? extends SuperBean> voClass) {
        return SqlHelper.sqlSession(voClass);
    }

    protected String sqlStatement(SqlMethod sqlMethod, Class<? extends SuperBean> voClass) {
        return this.sqlStatement(sqlMethod.getMethod(), voClass);
    }

    protected String sqlStatement(String sqlMethod, Class<? extends SuperBean> voClass) {
        return SqlHelper.table(voClass).getSqlStatement(sqlMethod);
    }

    protected void closeSqlSession(SqlSession sqlSession, Class<? extends SuperBean> voClass) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(voClass));
    }


}
