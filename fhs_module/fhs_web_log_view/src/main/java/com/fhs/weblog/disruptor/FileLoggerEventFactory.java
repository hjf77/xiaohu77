package com.fhs.weblog.disruptor;

import com.fhs.weblog.dto.FileLoggerEventDTO;
import com.lmax.disruptor.EventFactory;

/**
 * Created by kl on 2018/8/24.
 * Content :文件日志事件工厂类
 */
public class FileLoggerEventFactory implements EventFactory<FileLoggerEventDTO> {
    @Override
    public FileLoggerEventDTO newInstance() {
        return new FileLoggerEventDTO();
    }
}
