package com.fhs.core.beetl.config;
import com.fhs.common.utils.EMap;
import com.fhs.beetl.FhsBeetlClasspathResourceLoader;
import io.swagger.annotations.ApiOperation;
import org.beetl.core.Template;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * beetls 集成
 * by wanglei
 *
 * @author wanglei
 * @date 2020-05-19 13:52:30
 */
@Configuration
@Controller
@Component("fhsBeetlConfig")
public class BeetlConfig  {

    /**
     * 自身
     */
    public static BeetlConfig beetlConf;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BeetlGroupUtilConfiguration beetlGroupUtilConfiguration;

    @Bean(name = "beetlGroupUtilConfiguration")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        FhsBeetlClasspathResourceLoader classpathResourceLoader = new FhsBeetlClasspathResourceLoader();
        beetlGroupUtilConfiguration.setResourceLoader(classpathResourceLoader);
        beetlGroupUtilConfiguration.init();
        this.beetlGroupUtilConfiguration = beetlGroupUtilConfiguration;
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(
            @Qualifier("beetlGroupUtilConfiguration") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration, ContentNegotiatingViewResolver
            contentNegotiatingViewResolver) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setPrefix("/");
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        contentNegotiatingViewResolver.setOrder(1);
        beetlConf = this;
        return beetlSpringViewResolver;
    }

    /**
     * 渲染beetls 模板
     *
     * @param dir      路径
     * @param fileName 文件名称
     * @return ModelAndView 对象
     */
    @GetMapping("/b/{dir}/{fileName}")
    @ApiOperation("渲染beetl模板")
    public ModelAndView renderBeelts(@PathVariable("dir") String dir, @PathVariable("fileName") String fileName) {
        ModelAndView view = new ModelAndView();
        view.setViewName(dir.replace("-", "/") + "/" + fileName);
        return view;
    }


    /**
     * 渲染一个模板
     *
     * @param viewPath 模板路径
     * @param request  request
     * @return 渲染好的模板内容
     */
    public String renderBeelt(String viewPath, HttpServletRequest request) {
        Template template = beetlGroupUtilConfiguration.getGroupTemplate().getTemplate(viewPath);
        EMap<String, Object> parameterMap = getParameterMap();
        template.binding("parameter", parameterMap);
        return template.render();
    }

    /**
     * 渲染一个模板
     *
     * @param viewPath 模板路径
     * @return 渲染好的模板内容
     */
    public String renderBeelt(String viewPath, Map<String, Object> parameterMap) {
        Template template = beetlGroupUtilConfiguration.getGroupTemplate().getTemplate(viewPath);
        for (String key : parameterMap.keySet()) {
            template.binding(key, parameterMap.get(key));
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        template.binding("parameter", getParameterMap());
        return template.render();
    }

    /**
     * <获取参数map> 这里面有"group_code"和"projectIds"
     *
     * @return 参数map
     */
    public EMap<String, Object> getParameterMap() {
        HttpServletRequest request = getRequest();
        EMap<String, Object> resultMap = new EMap<>();
        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            resultMap.put(key, request.getParameter(key));
        }
        return resultMap;
    }

    /**
     * 获取request
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
