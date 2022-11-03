package com.fhs.basics.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.fhs.basics.vo.LogOperatorSysLogVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.api.context.BisLoggerContext;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.service.LogOperatorMainService;
import com.fhs.basics.vo.LogAddOperatorLogVO;
import com.fhs.basics.vo.LogOperatorMainVO;
import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.*;
import com.fhs.core.trans.vo.VO;
import com.fhs.log.LoggerContext;
import com.fhs.trans.service.impl.TransService;
import com.fhs.trans.utils.TransUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
@Slf4j
@Aspect
public class OperatorLogAop implements InitializingBean {


    @Autowired
    private TransService transService;

    @Autowired
    private LogOperatorMainService bisLoggerApiService;

    @Value("${easy-trans.is-enable-tile:false}")
    private Boolean isEnableTile;

    /**
     * 是否开启记录系统日志
     */
    @Value("${fhs.operator-log.sys-log-enable:false}")
    private Boolean isEnableSysLog;

    private Map<Method, Integer> VO_INDEX_MAP = new HashMap<>();


    /**
     * 定义切入点,所有标记这个的控制器
     */
    @Pointcut("@annotation(com.fhs.basics.api.anno.LogMethod)")
    public void operatorLogAopPoint() {

    }


    /**
     * 拦截每个Action的增删改查请求并向日志表里添加一条数据
     */
    @Around("operatorLogAopPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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
        String reqParamSource = null;
        try {
            LogMethod logMethod = method.getAnnotation(LogMethod.class);
            VO vo = getReqParamVO(joinPoint, method, logMethod);
            Object[] args = joinPoint.getArgs();
            if (vo != null) {
                reqParamSource = JsonUtils.bean2json(vo);
            } else if (logMethod.reqBodyParamIndex() != LoggerConstant.INDEX_NOT) {
                reqParamSource = JsonUtils.bean2json(args[logMethod.reqBodyParamIndex()]);
            } else {
                reqParamSource = JsonUtil.map2json(getParameterMap());
            }
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            final Object finalResult = result;
            final Exception finalError = error;

            addLog(joinPoint, method, classTarget, finalResult, finalError, reqParamSource);
            BisLoggerContext.clear();
        }
    }

    /**
     * 获取实际操作人
     *
     * @return
     */
    private String getDevOperator() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        String devOperator = servletRequestAttributes.getRequest().getHeader("devOperator");
        return devOperator == null ? null : URLDecoder.decode(devOperator);
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
    public void addLog(ProceedingJoinPoint joinPoint, Method method, Class<?> classTarget, Object result, Exception e, String reqParamSource) {
        LogMethod logMethod = method.getAnnotation(LogMethod.class);
        LogOperatorMainVO mainVO = new LogOperatorMainVO();
        HttpServletRequest request = getRequest();
        mainVO.setUrl(request.getRequestURI());
        mainVO.setLogId(BisLoggerContext.getTranceId());
        mainVO.setDevOperator(getDevOperator());
        try {
            mainVO.setIp(NetworkUtil.getIpAddress(request));
        } catch (IOException ex) {
            log.error("", ex);
        }
        mainVO.setReqMethod(request.getMethod());
        mainVO.setReqParamSource(reqParamSource);
        VO vo = getReqParamVO(joinPoint, method, logMethod);
        Object[] args = joinPoint.getArgs();
        String pkey = null;
        if (vo != null) {
            //创建代理对象平铺数据
            try {
                pkey = ConverterUtils.toString(vo.getPkey());
                vo = (VO) TransUtil.transOne(vo, transService, isEnableTile, new ArrayList<>());
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
            mainVO.setReqParam(BisLoggerContext.formartJson(JSONObject.toJSONString(vo), vo.getClass()));
        } else if (logMethod.reqBodyParamIndex() != LoggerConstant.INDEX_NOT) {
            mainVO.setReqParam(BisLoggerContext.formartJson(JSONObject.toJSONString(args[logMethod.reqBodyParamIndex()]), args[logMethod.reqBodyParamIndex()].getClass()));
        } else {
            mainVO.setReqParam(JsonUtil.map2json(getParameterMap()));
        }
        //记录主键
        if (!StringUtils.isEmpty(pkey) && logMethod.pkeyParamIndex() != LoggerConstant.INDEX_NOT) {
            pkey = ConverterUtils.toString(args[logMethod.pkeyParamIndex()]);
        }
        mainVO.setRespBody(e == null ? JSONObject.toJSONString(result) : e.getMessage());
        UcenterMsUserVO user = getSessionuser();
        mainVO.setPkeyStr(pkey);
        mainVO.preInsert(user.getUserId());
        mainVO.setType(logMethod.type());
        mainVO.setState(e == null ? LoggerConstant.LOG_STATE_SUCCESS : LoggerConstant.LOG_STATE_ERROR);
        LogNamespace logNamespace = classTarget.getAnnotation(LogNamespace.class);
        mainVO.setNamespace(Arrays.asList(logNamespace.namespace()).stream().collect(Collectors.joining(",")));

        LogAddOperatorLogVO operatorLogVO = new LogAddOperatorLogVO();
        if (BisLoggerContext.getSysLogBuilder().length() > 0) {
            LogOperatorSysLogVO operatorSysLogVO = new LogOperatorSysLogVO();
            operatorSysLogVO.setId(StringUtil.getUUID());
            operatorSysLogVO.setOperatorMainId(mainVO.getLogId());
            operatorSysLogVO.setSysLog(BisLoggerContext.getSysLogBuilder().toString());
            operatorLogVO.setOperatorSysLogVO(operatorSysLogVO);
        }
        operatorLogVO.setOperatorMainVO(mainVO);
        operatorLogVO.setHistoryDataVOList(BisLoggerContext.getLogHistoryDataVOList());
        operatorLogVO.setOperatorExtParamVOList(BisLoggerContext.getLogOperatorExtParamList());
        CompletableFuture.runAsync(() -> {
            this.addLog(operatorLogVO);
        });
    }


    /**
     * 异步入库
     *
     * @param log
     */
    private void addLog(LogAddOperatorLogVO log) {
        try {
            bisLoggerApiService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext.setProxyMehtod(BisLoggerContext.class.getMethod("addHistoryData", VO.class, String.class));
    }
}
