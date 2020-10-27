package com.fhs.core.valid.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 *
 * spring cloud接口调用参数校验aop
 * @author qh
 * @date 2020-05-19 09:54:36
 */
@Aspect
public class FeignParamCheckAop extends ParamCheckAop{



    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.*.*.api..*.*(..) )")
    public void checkParam(){
         //定义切入点表达式
    }


    @Before("checkParam()")
    public void doBefore(JoinPoint joinPoint) {
        //添加前置通知处理逻辑
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
        //添加返回后通知处理逻辑
    }
}
