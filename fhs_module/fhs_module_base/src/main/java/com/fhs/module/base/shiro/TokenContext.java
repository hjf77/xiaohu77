package com.fhs.module.base.shiro;

public class TokenContext {

    private static final ThreadLocal<String> TOKEN_CONTEXT = new ThreadLocal<>();

    /**
     * 设置token
     * @param token
     */
    public static void setToken(String token){
        TOKEN_CONTEXT.set(token);
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        return TOKEN_CONTEXT.get();
    }
}
