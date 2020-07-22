package com.fhs.core.exception;

import com.fhs.core.result.HttpResult;


/**
 * 由service层直接返回一个httpresult到前段
 * @author user
 * @date 2020-05-19 15:30:18
 */
public class ResultException extends RuntimeException {

    /**
     * 给前段返回的结果
     */
    private HttpResult<?> httpResult;

    public ResultException(HttpResult<?> httpResult) {
        this.httpResult = httpResult;
    }

    public HttpResult<?> getHttpResult(){
        return httpResult;
    }
}
