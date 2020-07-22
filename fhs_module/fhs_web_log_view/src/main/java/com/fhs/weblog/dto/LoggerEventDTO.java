package com.fhs.weblog.dto;

/**
 * Created by kl on 2018/8/24.
 * Content :进程日志事件内容载体
 */
public class LoggerEventDTO {

    private LoggerMessageDTO log;

    public LoggerMessageDTO getLog() {
        return log;
    }

    public void setLog(LoggerMessageDTO log) {
        this.log = log;
    }
}
