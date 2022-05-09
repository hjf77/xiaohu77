package com.fhs.core.exception.advice;

import cn.dev33.satoken.exception.NotLoginException;
import com.fhs.common.utils.AESUtil;
import com.fhs.common.utils.JsonUtil;
import com.fhs.common.utils.ThreadKey;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.*;
import com.fhs.core.result.HttpResult;
import com.fhs.core.result.PubResult;
import com.fhs.core.logger.Logger;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 控制器异常统一处理器
 * by wanglei
 */
@ControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * log
     */
    private static final Logger LOG = Logger.getLogger(ControllerExceptionAdvice.class);


    /**
     * 请求的 JSON 参数在请求体内的参数校验
     *
     * @param e 异常信息
     * @return 返回数据
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getField() + ":" + fieldError.getDefaultMessage() + ", ");
        }
        return HttpResult.validateError(null, errorMesssage.toString());
    }

    /**
     * 请求的 JSON 参数在请求体内的参数校验
     *
     * @param e 异常信息
     * @return 返回数据
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView bindException(HttpRequestMethodNotSupportedException e) {
        return HttpResult.validateError(null, "HTTP Method使用错了，详细信息:" + e.getMessage());
    }


    /**
     * 请求的 URL 参数检验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView bindViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> bindingResults = e.getConstraintViolations();
        StringBuilder errorMesssage = new StringBuilder("校验失败:");
        int size = 0;
        for (ConstraintViolation bindingResult : bindingResults) {
            errorMesssage.append(bindingResult.getPropertyPath() + ":" + bindingResult.getMessage());
            if (size != (bindingResults.size() - 1)) {
                errorMesssage.append(",");
            }
            size++;
        }
        return HttpResult.validateError(null, errorMesssage.toString());
    }

    /**
     * 请求的 URL 参数检验
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getField() + ":" + fieldError.getDefaultMessage() + ", ");
        }
        return HttpResult.validateError(null, errorMesssage.toString());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handlerMaxUploadSizeExceededException(Exception ex) {
        if (ex != null) {
            return resolveException(ex);
        }
        return new ModelAndView("error").addObject("msg", "未知错误：" + ex);
    }


    private ModelAndView resolveException(Exception ex) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        ModelAndView result = new ModelAndView();
        result.setViewName("platform/json");
        HttpResult httpResult = HttpResult.otherResult(PubResult.PARAM_ERROR);
        if (ex instanceof BaseException) {
            httpResult.setMessage(ex.getMessage());
        } else if (ex instanceof IllegalArgumentException) {
            httpResult.setMessage(ex.getMessage());
            JsonUtil.outJson(response, httpResult.asJson());
            return null;
        } else if (ex instanceof HttpException) {
            httpResult.setMessage(ex.getMessage());
            JsonUtil.outJson(response, httpResult.asJson(), ((HttpException) ex).getHttpCode());
            return null;
        } else if (ex instanceof ParamsInValidException) {
            httpResult.setMessage(ex.getMessage());
            JsonUtil.outJson(response, httpResult.asJson(),400);
            return null;
        } else if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) (ex)).getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder errMsg = new StringBuilder();
            for (ObjectError allError : allErrors) {
                FieldError fieldError = (FieldError) allError;
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                errMsg.append(field + ":" + defaultMessage + ";  ");
            }
            httpResult.setMessage(errMsg.toString());
            JsonUtil.outJson(response, httpResult.asJson(),400);
            return null;
        } else if (ex instanceof BusinessException) {
            LOG.error("处理客户端请求错误，客户端NONCE为：" + ThreadKey.BUS_KEY.get(), ex);
            httpResult.setMessage(ex.getMessage());
            httpResult.setCode(((BusinessException) ex).getCode());
            JsonUtil.outJson(response, httpResult.asJson());
            return null;
        } else if (ex instanceof ParamException) {
            httpResult.setMessage(ex.getMessage());
            httpResult.setCode(400);
            JsonUtil.outJson(response, httpResult.asJson(),400);
            return null;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            httpResult.setMessage("文件过大，请联系后台修改最大上传限制");
            httpResult.setCode(400);
            JsonUtil.outJson(response, httpResult.asJson(),400);
            return null;
        } else if (ex instanceof NotLoginException) {
            httpResult.setMessage(ex.getMessage());
            httpResult.setCode(401);
            JsonUtil.outJson(response, httpResult.asJson(),401);
            return null;
        } else if (ex instanceof CheckException) {
            JsonUtil.outJson(response, ((CheckException) ex).getResult().asJson());
            return null;
        } else if (ex instanceof DuplicateKeyException) {
            JsonUtil.outJson(response, HttpResult.otherResult(PubResult.PRIMARY_KEY_CONFLICT).asJson(),400);
            return null;
        } else if (ex instanceof SQLIntegrityConstraintViolationException) {
            JsonUtil.outJson(response, HttpResult.otherResult(PubResult.PRIMARY_KEY_CONFLICT).asJson(),400);
            return null;
        } else if (ex instanceof NotPremissionException) {
            JsonUtil.outJson(response, HttpResult.otherResult(PubResult.NO_PERMISSION).asJson(),403);
            return null;
        } else if (ex instanceof ResultException) {
            JsonUtil.outJson(response, ((ResultException) ex).getHttpResult().asJson());
            return null;
        } else {
            LOG.error("处理客户端请求错误，客户端NONCE为：" + ThreadKey.BUS_KEY.get(), ex);
            httpResult = HttpResult.otherResult(PubResult.SYSTEM_ERROR);
            httpResult.setMessage("系统错误，请联系管理员,NONCE:" + ThreadKey.BUS_KEY.get());
            if (EConfig.getOtherConfigPropertiesValue("exceptionInfoPassword") != null) {
                httpResult.setExceptionInfo(AESUtil.encrypt(getStackTrace(ex), EConfig.getOtherConfigPropertiesValue("exceptionInfoPassword")));
            }
            JsonUtil.outJson(response, httpResult.asJson(),500);
        }
        return null;
    }

    /**
     * 获取异常堆栈信息
     *
     * @param throwable 异常
     * @return 堆栈描述
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}