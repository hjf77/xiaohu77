package com.fhs.bislogger.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.api.context.BisLoggerContext;
import com.fhs.bislogger.api.rpc.FeignBisLoggerApiService;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.common.constant.Constant;
import com.fhs.basics.context.UserContext;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.*;
import com.fhs.core.trans.vo.VO;
import com.fhs.trans.service.impl.TransService;
import com.fhs.logger.Logger;
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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 拦截action请求，添加操作日志AOP
 *
 * @Filename: ActionInterceptAndCreateLogAop.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Aspect
public class OperatorLogAop {

    private static final Logger LOGGER = com.fhs.logger.Logger.getLogger(OperatorLogAop.class);

    @Autowired
    private TransService transService;

    private FeignBisLoggerApiService bisLoggerApiService;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();


    /**
     * 定义切入点,所有标记这个的控制器
     */
    @Pointcut("@annotation(com.fhs.bislogger.api.anno.LogMethod)")
    public void operatorLogAopPoint() {

    }


    /**
     * 拦截每个Action的增删改查请求并向日志表里添加一条数据
     */
    @Around("operatorLogAopPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (bisLoggerApiService == null) {
            bisLoggerApiService = SpringContextUtil.getBeanByClassForApi(FeignBisLoggerApiService.class);
        }
        String mainId = StringUtils.getUUID();
        BisLoggerContext.init(mainId);
        String methodName = joinPoint.getSignature().getName();
        Class<?> classTarget = joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = classTarget.getMethod(methodName, par);
        // 如果不包含直接放行
        if (!method.isAnnotationPresent(LogMethod.class) || !classTarget.isAnnotationPresent(LogNamespace.class)) {
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
            addLog(joinPoint, method, classTarget, result, error);
            BisLoggerContext.clear();
        }

    }


    /**
     * 添加操作日志
     *
     * @param joinPoint   切入点对象
     * @param method      方法
     * @param classTarget 目标类
     * @param result      方法返回值
     * @param e           异常
     */
    public void addLog(ProceedingJoinPoint joinPoint, Method method, Class<?> classTarget, Object result, Exception e) {
        LogMethod logMethod = method.getAnnotation(LogMethod.class);
        LogOperatorMainVO mainVO = new LogOperatorMainVO();
        HttpServletRequest request = getRequest();
        mainVO.setUrl(request.getRequestURI().toString());
        mainVO.setLogId(BisLoggerContext.getTranceId());
        try {
            mainVO.setIp(NetworkUtil.getIpAddress(request));
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
        mainVO.setReqMethod(request.getMethod());
        VO vo = getReqParamVO(joinPoint, method, logMethod);
        Object[] args = joinPoint.getArgs();
        if (vo != null) {
            this.transService.transOne(vo);
            mainVO.setReqParam(BisLoggerContext.formartJson(JSONObject.toJSONString(vo), vo.getClass()));
        } else if (logMethod.reqBodyParamIndex() != LoggerConstant.INDEX_NOT) {
            mainVO.setReqParam(BisLoggerContext.formartJson(JSONObject.toJSONString(args[logMethod.reqBodyParamIndex()]), args[logMethod.reqBodyParamIndex()].getClass()));
        } else {
            mainVO.setReqParam(JsonUtil.map2json(getParameterMap()));
        }
        mainVO.setRespBody(e == null ? JSONObject.toJSONString(result) : e.getMessage());
        UcenterMsUserVO user = getSessionuser();
        mainVO.preInsert(user.getUserId());
        mainVO.setType(logMethod.type());
        mainVO.setState(e == null ? LoggerConstant.LOG_STATE_SUCCESS : LoggerConstant.LOG_STATE_ERROR);
        LogNamespace logNamespace = classTarget.getAnnotation(LogNamespace.class);
        mainVO.setModel(logNamespace.module());
        mainVO.setNamespace(logNamespace.namespace());
        LogAddOperatorLogVO operatorLogVO = new LogAddOperatorLogVO();
        operatorLogVO.setOperatorMainVO(mainVO);
        operatorLogVO.setHistoryDataVOList(BisLoggerContext.getLogHistoryDataVOList());
        operatorLogVO.setOperatorExtParamVOList(BisLoggerContext.getLogOperatorExtParamList());
        this.addLog(operatorLogVO);
    }


    /**
     * 异步入库
     *
     * @param log
     */
    private void addLog(LogAddOperatorLogVO log) {
        singleThreadExecutor.submit(() -> {
            try {
                bisLoggerApiService.addLog(log);
            } catch (Exception e) {
                LOGGER.error("日志插入错误", e);
            }

        });
    }


    /**
     * 获取session里面的user
     *
     * @return session里面的user
     */
    protected UcenterMsUserVO getSessionuser() {
        return UserContext.getSessionuser();
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
            int voIndex = logMethod.voParamIndex();
            if (voIndex > args.length - 1) {
                voIndex = args.length - 1;
            }
            return (VO) args[voIndex];
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


}
