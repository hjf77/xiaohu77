package com.fhs.flow.exception;

import com.fhs.core.result.HttpResult;
import org.flowable.common.engine.api.FlowableException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Flowable 异常处理
 * by wanglei
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class FlowableExceptionAdvice {

    @ExceptionHandler(FlowableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView flowableException(FlowableException ex) {
        return HttpResult.validateError(null, ex.getCause().getMessage());
    }
}
