package com.fhs.weblog.disruptor;

import com.fhs.weblog.dto.LoggerEventDTO;
import com.lmax.disruptor.EventFactory;

/**
 * Created by kl on 2018/8/24.
 * Content :进程日志事件工厂类
 */
public class LoggerEventFactory implements EventFactory<LoggerEventDTO> {
    @Override
    public LoggerEventDTO newInstance() {
        return new LoggerEventDTO();
    }
}
