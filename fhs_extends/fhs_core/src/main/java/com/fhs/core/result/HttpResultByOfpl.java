package com.fhs.core.result;

import com.fhs.core.base.pojo.BaseObject;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
 * @author LiYuLin
 * @version 1.0
 * @description: TODO
 * @date 2022/2/24 9:36
 */
public class HttpResultByOfpl<V> extends BaseObject<HttpResultByOfpl> {


    /**
     * 编码
     */
    private int code;


    /**
     * 信息
     */
    private String msg = "";


    /**
     * 成功
     *
     * @since 1.0.0
     */
    public static int SUCCESSFUL = 200;


    /**
     * 有错误
     *
     * @since 1.0.0
     */
    public static int ERRORS = 500;




/*    private String character = "";

    private int check;

    private String treatment = "";*/

    /**
     * 成功 只返回code 和 message
     */
    public static HttpResultByOfpl<Map> OK(String message) {
        HttpResultByOfpl<Map> result = new HttpResultByOfpl<>();
        result.code = SUCCESSFUL;
        result.msg = message;
        return result;
    }

    /**
     * 失败 只返回code 和 message
     */
    public static HttpResultByOfpl<Map> fail(int code, String message) {
        HttpResultByOfpl<Map> result = new HttpResultByOfpl<Map>();
        result.code = code;
        result.msg = message;
        return result;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
