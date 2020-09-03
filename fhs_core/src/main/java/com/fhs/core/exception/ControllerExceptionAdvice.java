package com.fhs.core.exception;

import com.fhs.common.utils.AESUtil;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.Logger;
import com.fhs.common.utils.ThreadKey;
import com.fhs.core.config.EConfig;
import com.fhs.core.result.HttpResult;
import com.fhs.core.result.PubResult;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 控制器异常统一处理器
 * by wanglei
 */
@ControllerAdvice
public class ControllerExceptionAdvice {

    /** log */
    private static final Logger LOG = Logger.getLogger(ControllerExceptionAdvice.class);


    @ExceptionHandler(Exception.class)
    public ModelAndView handlerMaxUploadSizeExceededException(Exception ex) {
        if (ex != null) {
            return resolveException(ex);
        }
        return new ModelAndView("error").addObject("msg", "未知错误：" + ex);
    }


    private ModelAndView resolveException(Exception ex)
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        ModelAndView result = new ModelAndView();
        result.setViewName("platform/json");
        HttpResult httpResult = HttpResult.otherResult(PubResult.PARAM_ERROR);
        if (ex instanceof BaseException)
        {
            httpResult.setMessage(ex.getMessage());
        }
        else if (ex instanceof IllegalArgumentException)
        {
            httpResult.setMessage(ex.getMessage());
            JsonUtils.outJson(response, httpResult.asJson());
            return null;
        }
        else if (ex instanceof HttpException)
        {
            httpResult.setMessage(ex.getMessage());
            JsonUtils.outJson(response, httpResult.asJson(), ((HttpException)ex).getHttpCode());
            return null;
        }
        else if (ex instanceof BusinessException)
        {
            LOG.error("处理客户端请求错误，客户端NONCE为：" + ThreadKey.BUS_KEY.get(), ex);
            httpResult.setMessage(ex.getMessage());
            httpResult.setCode (((BusinessException) ex).getCode ());
            JsonUtils.outJson(response, httpResult.asJson());
            return null;
        }
        else if (ex instanceof ParamException)
        {
            httpResult.setMessage(ex.getMessage());
            httpResult.setCode (400);
            httpResult.setData("");
            JsonUtils.outJson(response, httpResult.asJson());
            return null;
        }
        else if (ex instanceof CheckException)
        {
            JsonUtils.outJson(response, ((CheckException)ex).getResult().asJson());
            return null;
        }
        else if (ex instanceof DuplicateKeyException)
        {
            JsonUtils.outJson(response, HttpResult.otherResult(PubResult.PRIMARY_KEY_CONFLICT).asJson());
            return null;
        }
        else if (ex instanceof MySQLIntegrityConstraintViolationException)
        {
            JsonUtils.outJson(response, HttpResult.otherResult(PubResult.PRIMARY_KEY_CONFLICT).asJson());
            return null;
        }
        else if (ex instanceof NotPremissionException)
        {
            JsonUtils.outJson(response, HttpResult.otherResult(PubResult.NO_PERMISSION).asJson());
            return null;
        }
        else if (ex instanceof ResultException)
        {
            JsonUtils.outJson(response, ((ResultException)ex).getHttpResult().asJson());
            return null;
        }
        else
        {

            LOG.error("处理客户端请求错误，客户端NONCE为：" + ThreadKey.BUS_KEY.get(), ex);
            httpResult = HttpResult.otherResult(PubResult.SYSTEM_ERROR);
            httpResult.setMessage("系统错误，请联系管理员,NONCE:" + ThreadKey.BUS_KEY.get() );
            httpResult.setExceptionInfo(AESUtil.encrypt(getStackTrace(ex), EConfig.getOtherConfigPropertiesValue("exceptionInfoPassword")));
            JsonUtils.outJson(response, httpResult.asJson());
        }
        return null;
    }

    /**
     * 获取异常堆栈信息
     * @param throwable 异常
     * @return 堆栈描述
     */
    public static String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }
}
