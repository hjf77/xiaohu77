package com.fhs.core.valid.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 其他的控制器校验aop
 * @author user
 * @date 2020-05-19 09:55:26
 */
@Aspect
public class OrdinaryActionParamCheckAop extends ParamCheckAop {
    /**
     * 定义切入点
     * com.xhb.tour.controller
     */
    @Pointcut("execution(* com.*.*.action..*.*(..) )")
    public void checkParam(){
            //定义切入点表达式
    }

    @Before("checkParam()")
    public void doBefore(JoinPoint joinPoint) {
        //前置通知逻辑处理
    }

    /**
     * 参数校验
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("checkParam()")
    @Override
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.doAround(joinPoint);
    }
    /**
     * 在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
     *
     * @param joinPoint
     */
    @AfterReturning("checkParam()")
    public void doAfterReturning(JoinPoint joinPoint) {
        //返回通知逻辑处理
    }


}
