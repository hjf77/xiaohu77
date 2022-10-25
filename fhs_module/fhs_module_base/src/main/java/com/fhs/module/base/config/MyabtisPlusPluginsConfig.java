package com.fhs.module.base.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.conditions.AbstractJoinWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.JoinInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MyabtisPlusPluginsConfig {
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        interceptor.addInnerInterceptor(new FhsJoinInterceptor());
        return interceptor;
    }


}

class FhsJoinInterceptor extends JoinInterceptor {
    /**
     * 缓存MappedStatement,不需要每次都去重写构建MappedStatement
     */
    private static final Map<String, Map<org.apache.ibatis.session.Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();

    @Override
    public MappedStatement changeMappedStatement(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (parameter != null && parameter instanceof Map) {
            if (((Map) parameter).containsKey("ew")) {
                Object ew = ((Map) parameter).get("ew");
                if (ew != null && ew instanceof AbstractJoinWrapper && ms.getResultMaps() != null && !ms.getResultMaps().isEmpty()) {
                    return this.newMappedStatement(ms, ((ResultMap) ms.getResultMaps().get(0)).getType());
                }
            }
        }
        return ms;

    }


}