package com.fhs.core.result;

import com.fhs.core.base.pojo.BaseObject;

import java.util.Map;

/**
 * @author LiYuLin
 * @version 1.0
 * @description: TODO
 * @date 2022/2/24 12:01
 */
public class HttpResultByAlarm<V> extends BaseObject<HttpResultByAlarm> {

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

    private String character = "";

    private int check;

    private String treatment = "";


    public static HttpResultByAlarm<Map> OK(String message, String character, int check, String treatment) {
        HttpResultByAlarm<Map> result = new HttpResultByAlarm<>();
        result.code = SUCCESSFUL;
        result.msg = message;
        result.setCharacter(character);
        result.setCheck(check);
        result.setTreatment(treatment);
        return result;
    }

    /*
     * 失败 只返回code 和 message
     */
    public static HttpResultByAlarm<Map> fail(int code, String message,String character, int check, String treatment) {
        HttpResultByAlarm<Map> result = new HttpResultByAlarm<Map>();
        result.code = code;
        result.msg = message;
        result.setCharacter(character);
        result.setCheck(check);
        result.setTreatment(treatment);
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

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
