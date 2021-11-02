package com.fhs.beetl.common;

import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.EMap;
import org.beetl.core.Template;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * beetl工具类
 *
 * @author user
 * @date 2020-05-19 13:38:29
 */
public class BeetlUtil {

    private static BeetlGroupUtilConfiguration beetlGroupUtilConfiguration;

    /**
     * 渲染一个模板
     *
     * @param viewPath 模板路径
     * @return 渲染好的模板内容
     */
    public static String renderBeelt(String viewPath, Map<String, Object> parameterMap) {
        if (beetlGroupUtilConfiguration == null) {
            beetlGroupUtilConfiguration = SpringContextUtil.getBeanByName(BeetlGroupUtilConfiguration.class);
        }
        Template template = beetlGroupUtilConfiguration.getGroupTemplate().getTemplate(viewPath);
        for (String key : parameterMap.keySet()) {
            template.binding(key, parameterMap.get(key));
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request!= null) {
            template.binding("parameter", getParameterMap(request));
        }
        return template.render();
    }


    private static EMap<String, Object> getParameterMap(HttpServletRequest request) {
        EMap<String, Object> resultMap = new EMap<String, Object>();
        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            resultMap.put(key, request.getParameter(key));
        }

        return resultMap;
    }
}
