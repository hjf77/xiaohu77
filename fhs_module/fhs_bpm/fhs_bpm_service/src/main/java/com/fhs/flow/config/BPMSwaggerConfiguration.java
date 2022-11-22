package com.fhs.flow.config;

import com.fhs.module.base.swagger.anno.ApiGroup;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

/**
 * swagger配置
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.base.action
 * @ClassName: SwaggerConfiguration
 * @Author: JackWang
 * @CreateDate: 2018/9/9 0009 14:52
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/9/9 0009 14:52
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "fhs.swagger", name = "enable", havingValue = "true", matchIfMissing = false)
public class BPMSwaggerConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware {
    /**
     * 基础包
     */
    protected String basePackage;
    /**
     * 创建人
     */
    protected String creatName;
    /**
     * 服务名称
     */
    protected String serviceName;

    /**
     * 配置文件获取
     */
    protected Environment environment;
    /**
     * 文档描述
     */
    protected String description;

    /**
     * 默认分组
     */
    private static final String BPM = "bpm";


    public BPMSwaggerConfiguration() {
    }




    /**
     * api信息
     *
     * @return
     */
    protected ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title(this.serviceName + " Restful APIs").description(this.description).contact(this.creatName).version("1.0").build();
    }


    /**
     * 非spirngcloud模式下启用
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingClass("com.alibaba.cloud.nacos.NacosConfigAutoConfiguration")
    public Docket bpmApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("工作流")
                .apiInfo(this.apiInfo()).useDefaultResponseMessages(false).forCodeGeneration(false)
                .select().apis(this.getPredicateWithGroup(BPM)).paths(PathSelectors.any()).build().securityContexts(Lists.newArrayList(new SecurityContext[]{this.securityContext()})).securitySchemes(Lists.newArrayList(new SecurityScheme[]{this.apiKey()}));
    }

    @Bean
    @ConditionalOnClass(name = "com.alibaba.cloud.nacos.NacosConfigAutoConfiguration")
    public Docket bpmRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(this.getPredicateWithGroup(BPM))
                .paths(PathSelectors.any())
                .build().securityContexts(Lists.newArrayList(new SecurityContext[]{this.securityContext()})).securitySchemes(Lists.newArrayList(new SecurityScheme[]{this.apiKey()}))
                //不要尝试注释掉此行，网关集成的时候会报错
                .host("http://fhs-opensource.hello");
    }

    private Predicate<RequestHandler> getPredicateWithGroup(final String group) {
        return new Predicate<RequestHandler>() {
            public boolean apply(RequestHandler input) {
                Optional<ApiGroup> ApiGroup = input.findControllerAnnotation(ApiGroup.class);
                return ApiGroup.isPresent() && Arrays.asList(((ApiGroup) ApiGroup.get()).group()).contains(group);
            }
        };
    }


    private ApiKey apiKey() {
        return new ApiKey("Authorization", "token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Lists.newArrayList(new SecurityReference[]{new SecurityReference("Authorization", authorizationScopes)});
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.basePackage = this.environment.getProperty("fhs.swagger.basepackage");
        this.creatName = this.environment.getProperty("fhs.swagger.service.developer");
        this.serviceName = this.environment.getProperty("fhs.swagger.service.name");
        this.description = this.environment.getProperty("fhs.swagger.service.description");
    }


}
