package com.fhs.core.jsonfilter.aop;

import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.jsonfilter.anno.AutoArray;
import lombok.Data;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Aspect
public class ParamArrayHandle {

    /**
     * 定义切入点
     * 所有的控制器方法
     */
    @Pointcut("execution(* com..controller.*.*(..) )")
    public void contorllerPointcout(){

    }

    /**
     * 纪录方法 的第几个参数是需要 array2string的
     */
    private Map<String, AutoArraySett> parameterSettMaps = new HashedMap();

    /**
     * 处理 array2str
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("contorllerPointcout()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> classTarget = joinPoint.getTarget().getClass();
        String cacheKey = classTarget.getName() + "_" + joinPoint.getSignature().getName();
        AutoArraySett autoArraySett = null;
        Object[] params = joinPoint.getArgs();
       /* if (parameterSettMaps.containsKey(cacheKey) && parameterSettMaps.get(cacheKey) != null) {
            autoArraySett = parameterSettMaps.get(cacheKey);
        } else if (parameterSettMaps.containsKey(cacheKey) && parameterSettMaps.get(cacheKey) == null) {
            return joinPoint.proceed();
        } else {*/
            autoArraySett = this.getMethodAutoArray(params);
            parameterSettMaps.put(cacheKey, autoArraySett);
        //}
        if (autoArraySett != null) {
            Object needHandleParam = params[autoArraySett.getIndex()];
            if(needHandleParam == null){
                return joinPoint.proceed();
            }
            List<Field> fields = autoArraySett.getAutoArrayFields();
            for (Field field : fields) {
                String strVal = ConverterUtils.toString(ReflectUtils.getValue(needHandleParam,field.getName()));
                if(!StringUtils.isEmpty(strVal) && strVal.length()>2){
                    //如果不是[ 开头的代表已经是好着的了，不需要这里处理
                    if(!strVal.startsWith("[")){
                        continue;
                    }
                    ReflectUtils.setValue(needHandleParam,field, JsonUtils.parseArray(strVal));
                }
            }
        }

        return joinPoint.proceed();
    }



    /**
     * @param params
     * @return
     */
    public AutoArraySett getMethodAutoArray(Object[] params) {
        AutoArraySett result = null;
        // 遍历所有的参数，找到需要校验的参数，进行校验并且记录校验结果
        for (int i =0;i<params.length;i++) {
            if (null ==  params[i]) {
                continue;
            }
            if (params[i] instanceof VO) {
                List<Field> autoArrayFields = ReflectUtils.getAnnotationField(params[i].getClass(), AutoArray.class);
                if (!autoArrayFields.isEmpty()) {
                    result = new AutoArraySett();
                    result.setAutoArrayFields(autoArrayFields);
                    result.setIndex(i);
                    return result;
                }
            }
            continue;
        }
        return result;
    }

}

/**
 * 自动array自动转换
 */
@Data
class AutoArraySett {
    /**
     * 哪些字段需要 手动处理
     */
    private List<Field> autoArrayFields;

    /**
     * 索引
     */
    private int index;
}
