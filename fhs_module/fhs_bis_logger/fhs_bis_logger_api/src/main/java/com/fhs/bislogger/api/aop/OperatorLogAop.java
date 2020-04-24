package com.fhs.bislogger.api.aop;

import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.logger.Logger;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 拦截action请求，添加操作日志AOP
 *
 * @Filename: ActionInterceptAndCreateLogAop.java
 * @Description:
 * @Version: 1.0
 * @Author: qixiaobo
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Aspect
public class OperatorLogAop {

    private static final Logger LOGGER = com.fhs.logger.Logger.getLogger(OperatorLogAop.class);

    @Autowired
    private TransService transService;

    /**
     * 定义切入点,所有标记这个的控制器
     */
    @Pointcut("@annotation(com.fhs.bislogger.api.anno.LogNamespace)")
    public void operatorLogAopPoint() {

    }


    /**
     * 拦截每个Action的增删改查请求并向日志表里添加一条数据
     */
    @Around("operatorLogAopPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?> classTarget = joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = classTarget.getMethod(methodName, par);
        // 如果不包含直接放行
        if (!method.isAnnotationPresent(LogMethod.class)) {
            return joinPoint.proceed();
        }
        Object result = null;
        Exception error = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {


        }



        /*
          新增,如果是vo



         */

       /* // 获取请求uri
        String uri = request.getRequestURI();
        // 如果不是以ms开头就不拦截
        if (!uri.contains("/ms/")) {
            // 执行方法
            return joinPoint.proceed();
        }

        // 获取菜单name及nameSpace
        if (namesapceMenuMap.isEmpty()) {
            HttpResult<List<SysMenuVo>> result = feignSysMenuApiService.findIdAndNameAndNamespaceList();
            List<SysMenuVo> sysMenuList = result.getData();
            for (SysMenuVo adminMenu : sysMenuList) {
                namesapceMenuMap.put(adminMenu.getNamespace(), adminMenu);
            }
        }

        // 判断方法上是否包含LogDesc注解
        if (method.isAnnotationPresent(LogDesc.class)) {
            // 获取该注解的内容
            LogDesc annotation = method.getAnnotation(LogDesc.class);
            String operation = annotation.value();
            int type = annotation.type();

            // 获取请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            String paramsJson = "";
            if (parameterMap.containsKey("password")) {
                Map<String, String[]> parameterUnLockMap = new HashMap<>();
                for(Map.Entry<String, String[]> entry : parameterMap.entrySet()){
                    if(!entry.getKey().equals("password")) {
                        parameterUnLockMap.put(entry.getKey(), entry.getValue());
                    }
                }
                paramsJson = JsonUtils.map2json(parameterUnLockMap);
            }else {
                paramsJson = JsonUtils.map2json(parameterMap);
            }
            //获取用户ip
            String ip = NetworkUtil.getIpAddress(request);
            // 获取请求用户id
            SysUserVo adminUser = (SysUserVo) request.getSession().getAttribute(Constant.SESSION_USER);
            String userId = adminUser.getUserId();

            // 创建LogAdminOperatorLog 对象,并给各属性赋值
            LogAdminOperatorLogVo log = new LogAdminOperatorLogVo();
            log.setCreateTime(DateUtils.getCurrentDateStr(DateUtils.DATE_FULL_STR_SSS));
            log.setUrl(uri);
            log.setOperatorId(userId);
            log.setReqParam(paramsJson);
            log.setNetworkIp(ip);
            log.setLogType(type);
            log.setGroupCode(adminUser.getGroupCode());
            String namespace = uri.substring(uri.indexOf("/", uri.indexOf("/") + 1) + 1, uri.lastIndexOf("/"));
            SysMenuVo menu = namesapceMenuMap.get(namespace);
            if (menu != null) {
                log.setMenuId(menu.getMenuId());
                log.setOperatDesc(menu.getMenuName() + ":" + operation);
                feignlogAdminOperatorLogApiService.addLogAdminOperatorLog(log);
            } else {
                LOGGER.warnMsg("namespace:{} 没有找到对应的菜单", namespace);
            }
            // 执行方法
            return joinPoint.proceed();
        }
        return joinPoint.proceed();*/
    }


    public void addLog(ProceedingJoinPoint joinPoint, Method method, Class<?> classTarget, Object result, Exception e) {
        LogMethod logMethod = method.getAnnotation(LogMethod.class);
        LogOperatorMainVO mainVO = new LogOperatorMainVO();
        HttpServletRequest request = getRequest();
        mainVO.setUrl(request.getRequestURL().toString());
        try {
            mainVO.setIp(NetworkUtil.getIpAddress(request));
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
        mainVO.setReqMethod(request.getMethod());
        VO vo = getReqParamVO(joinPoint, method, logMethod);
        if (vo != null) {
            this.transService.transOne(vo);
            mainVO.setReqParam(formartJson(JsonUtils.bean2json(vo), vo.getClass()));
        } else if (logMethod.reqBodyParamIndex() != LoggerConstant.INDEX_NOT) {
            Object[] args = joinPoint.getArgs();
            mainVO.setReqParam(formartJson(JsonUtils.bean2json(args[logMethod.reqBodyParamIndex()]), args[logMethod.reqBodyParamIndex()].getClass()));
        } else {
            mainVO.setReqParam(JsonUtils.map2json(getParameterMap()));
        }
        mainVO.setRespBody(result != null ? JsonUtils.object2json(request) : e.getMessage());
        UcenterMsUserVO user = getSessionuser();
        mainVO.preInsert(user.getUserId());
        mainVO.setType(logMethod.type());

    }


    public LogAddOperatorLogVO handleAdd(VO vo) {
        LogAddOperatorLogVO result = new LogAddOperatorLogVO();
        if (vo != null) {
            LogHistoryDataVO historyDataVO = new LogHistoryDataVO();

        }
        return result;
    }

    /**
     * 获取session里面的user
     *
     * @return session里面的user
     */
    protected UcenterMsUserVO getSessionuser() {
        HttpServletRequest request = getRequest();
        return (UcenterMsUserVO) request.getSession().getAttribute(Constant.SESSION_USER);
    }

    /**
     * <获取参数map> 这里面有"group_code"和"projectIds"
     *
     * @return 参数map
     */
    public EMap<String, Object> getParameterMap() {
        HttpServletRequest request = getRequest();
        EMap<String, Object> resultMap = new EMap<String, Object>();
        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            resultMap.put(key, request.getParameter(key));
        }
        return resultMap;
    }

    /**
     * 格式化json
     *
     * @param json  json
     * @param clazz 类
     * @return
     */
    public String formartJson(String json, Class clazz) {
        List<Field> fieldList = ReflectUtils.getAnnotationField(clazz, ApiModelProperty.class);
        ApiModelProperty apiModelProperty = null;
        for (Field field : fieldList) {
            apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            json = json.replace(field.getName(), apiModelProperty.value());
        }
        return json;
    }


    /**
     * 获取request
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取调用此方法的参数
     *
     * @param joinPoint
     * @return
     */
    public VO getReqParamVO(ProceedingJoinPoint joinPoint, Method method, LogMethod logMethod) {
        // 获取所有的参数的注解
        Object[] args = joinPoint.getArgs();
        if (logMethod.voParamIndex() != LoggerConstant.INDEX_NOT) {
            return (VO) args[logMethod.voParamIndex()];
        }
        if (VO_INDEX_MAP.containsKey(method)) {
            return (VO) args[VO_INDEX_MAP.get(method)];
        }
        int index = 0;
        for (Object param : args) {
            if (param instanceof VO) {
                VO_INDEX_MAP.put(method, index);
                return (VO) param;
            }
            index++;
        }
        return null;
    }

    private Map<Method, Integer> VO_INDEX_MAP = new HashMap<>();

    /* Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations != null && parameterAnnotations.length != 0) {
            int index = 0;
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    // 如果用此标记代表是参数
                    if (annotation instanceof LogParam) {

                    }
                }
                index++;
            }
        }
        return "";*/

}
