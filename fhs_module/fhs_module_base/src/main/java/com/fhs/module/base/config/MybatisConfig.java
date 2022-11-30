package com.fhs.module.base.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.config.EConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * mybatis jpa 配置
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.config
 * @ClassName: MybatisConfig
 * @Author: JackWang
 * @CreateDate: 2018/8/15 0015 16:04
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/8/15 0015 16:04
 * @Version: 1.0
 */
@Slf4j
@org.springframework.context.annotation.Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class MybatisConfig implements MetaObjectHandler {


    @Bean("fhsXMLMapperLoader")
    public XMLMapperLoader getXMLMapperLoader(MybatisPlusProperties plusProperties) {
        XMLMapperLoader loader = new XMLMapperLoader();
        loader.setEnabled(ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isDevModel")));//开启xml热加载
        loader.setMapperLocations(plusProperties.resolveMapperLocations());
        log.info("xml刷新器初始化:" + plusProperties.getMapperLocations() + "--" + loader.getMapperLocations());
        return loader;
    }

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = UserContext.getSessionuser() != null ? UserContext.getSessionuser().getUserId() : null;
        //字段填充
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("createUser", userId, metaObject);
        this.setFieldValByName("updateUser", userId, metaObject);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = UserContext.getSessionuser() != null ? UserContext.getSessionuser().getUserId() : null;
        //字段填充
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateUser", userId, metaObject);
    }
}


/**
 * xml mp 热加载支持 by wanglei
 *
 * @ 本代码由https://my.oschina.net/houke/blog/901909 copy修改
 */
@Slf4j
class XMLMapperLoader implements DisposableBean, InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(XMLMapperLoader.class);

    private ApplicationContext applicationContext;
    private ScheduledExecutorService executorService;

    private Long initialDelay = 5L;

    private Long period = 5L;

    /**
     * 是否启用
     */
    private boolean enabled = true;

    private Resource[] mapperLocations;

    public XMLMapperLoader() {
        super();
        logger.debug("MyBatis xml文件热部署模块初始完成，生产模式需要移除");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            Map<String, SqlSessionFactory> sqlSessionFactoryBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, SqlSessionFactory.class);
            if (sqlSessionFactoryBeans != null && sqlSessionFactoryBeans.size() > 0) {
                executorService = Executors.newScheduledThreadPool(sqlSessionFactoryBeans.size());
                for (SqlSessionFactory sqlSessionFactoryBean : sqlSessionFactoryBeans.values()) {
                    Scanner scanner = new Scanner(sqlSessionFactoryBean.getConfiguration(), mapperLocations);
                    executorService.scheduleAtFixedRate(scanner, initialDelay, period, TimeUnit.SECONDS);
                }
            }
        }
    }


    class Scanner implements Runnable {

        private Configuration configuration;
        private Resource[] mapperLocations;
        private HashMap<String, String> mapperFiles = new HashMap<String, String>();

        private Set<?> loadedResources;
        private Map<?, ?> knownMappers;
        private Collection<String> cacheNames;
        private Collection<String> mappedStatementNames;
        private Collection<String> parameterMapNames;
        private Collection<String> resultMapNames;
        private Collection<String> sqlFragmentNames;
        private Collection<String> keyGeneratorNames;

        public Scanner(Configuration configuration, Resource[] mapperLocations) {
            this.configuration = configuration;
            this.mapperLocations = mapperLocations;
            loadedResources = getSetField(Configuration.class, configuration, "loadedResources");
            knownMappers = getMapField(MapperRegistry.class, configuration.getMapperRegistry(), "knownMappers");
            cacheNames = configuration.getCacheNames();
            mappedStatementNames = configuration.getMappedStatementNames();
            parameterMapNames = configuration.getParameterMapNames();
            resultMapNames = configuration.getResultMapNames();
            keyGeneratorNames = configuration.getKeyGeneratorNames();
            sqlFragmentNames = configuration.getSqlFragments().keySet();
            this.scan();
        }

        @Override
        public void run() {
            List<Resource> resources = this.getChangedXml();
            if (resources != null && resources.size() > 0) {
                this.reloadXML(resources);
            }
        }

        public void reloadXML(List<Resource> resources) {
            for (Resource mapperLocation : resources) {
                refresh(mapperLocation);
            }
        }

        private void refresh(Resource resource) {
            try {
                XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
                XNode context = xPathParser.evalNode("/mapper");
                String namespace = context.getStringAttribute("namespace");
                knownMappers.remove(Resources.classForName(namespace));
                loadedResources.remove(resource.toString());
                cacheNames.remove(namespace);
                cleanMappedStatements(context.evalNodes("select|insert|update"), namespace);
                cleanParameterMaps(context.evalNodes("/mapper/parameterMap"), namespace);
                cleanResultMaps(context.evalNodes("/mapper/resultMap"), namespace);
                cleanKeyGenerators(context.evalNodes("insert|update"), namespace);
                cleanSqlElements(context.evalNodes("/mapper/sql"), namespace);
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments());
                xmlMapperBuilder.parse();
                logger.debug("xml refresh success:" + namespace);
            } catch (Exception e) {
                logger.error("Refresh IOException :" + e.getMessage());
            } finally {
                ErrorContext.instance().reset();
            }
        }

        private Set<?> getSetField(Class<?> clazz, Object object, String fieldName) {
            try {
                Field setField = clazz.getDeclaredField(fieldName);
                setField.setAccessible(true);
                return (Set<?>) setField.get(object);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private Map<?, ?> getMapField(Class<?> clazz, Object object, String fieldName) {
            try {
                Field setField = clazz.getDeclaredField(fieldName);
                setField.setAccessible(true);
                return (Map<?, ?>) setField.get(object);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        /**
         * 清理mappedStatements
         *
         * @param list
         * @param namespace
         */
        private void cleanMappedStatements(List<XNode> list, String namespace) {
            for (XNode node : list) {
                String id = node.getStringAttribute("id");
                mappedStatementNames.remove(namespace + "." + id);
            }
        }

        /**
         * 清理parameterMap
         *
         * @param list
         * @param namespace
         */
        private void cleanParameterMaps(List<XNode> list, String namespace) {
            for (XNode node : list) {
                String id = node.getStringAttribute("id");
                parameterMapNames.remove(namespace + "." + id);
            }
        }

        /**
         * 清理resultMap
         *
         * @param list
         * @param namespace
         */
        private void cleanResultMaps(List<XNode> list, String namespace) {
            for (XNode node : list) {
                String id = node.getStringAttribute("id", node.getValueBasedIdentifier());
                resultMapNames.remove(id);
                resultMapNames.remove(namespace + "." + id);
                clearResultMaps(node, namespace);
            }
        }

        private void clearResultMaps(XNode xNode, String namespace) {
            for (XNode node : xNode.getChildren()) {
                if ("association".equals(node.getName()) || "collection".equals(node.getName()) || "case".equals(node.getName())) {
                    if (node.getStringAttribute("select") == null) {
                        resultMapNames.remove(node.getStringAttribute("id", node.getValueBasedIdentifier()));
                        resultMapNames.remove(namespace + "." + node.getStringAttribute("id", node.getValueBasedIdentifier()));
                        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                            clearResultMaps(node, namespace);
                        }
                    }
                }
            }
        }

        /**
         * 清理selectKey
         *
         * @param list
         * @param namespace
         */
        private void cleanKeyGenerators(List<XNode> list, String namespace) {
            for (XNode node : list) {
                String id = node.getStringAttribute("id");
                keyGeneratorNames.remove(id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
                keyGeneratorNames.remove(namespace + "." + id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
            }
        }

        /**
         * 清理sql节点缓存
         *
         * @param list
         * @param namespace
         */
        private void cleanSqlElements(List<XNode> list, String namespace) {
            for (XNode node : list) {
                String id = node.getStringAttribute("id");
                sqlFragmentNames.remove(id);
                sqlFragmentNames.remove(namespace + "." + id);
            }
        }

        public void scan() {
            if (!mapperFiles.isEmpty()) {
                return;
            }
            for (Resource mapperLocation : this.mapperLocations) {
                String fileKey = getValue(mapperLocation);
                mapperFiles.put(mapperLocation.getFilename(), fileKey);
            }
        }

        private String getValue(Resource resource) {
            try {
                String contentLength = String.valueOf((resource.contentLength()));
                String lastModified = String.valueOf((resource.lastModified()));
                return new StringBuilder(contentLength).append(lastModified).toString();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public List<Resource> getChangedXml() {
            List<Resource> resources = new ArrayList<Resource>();
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation == null) {
                    continue;
                }
                String name = mapperLocation.getFilename();
                String value = mapperFiles.get(name);
                String fileKey = getValue(mapperLocation);
                if (!fileKey.equals(value)) {
                    mapperFiles.put(name, fileKey);
                    resources.add(mapperLocation);
                }
            }
            return resources;
        }
    }

    public void destroy() throws Exception {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public void setInitialDelay(Long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Resource[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }
}
