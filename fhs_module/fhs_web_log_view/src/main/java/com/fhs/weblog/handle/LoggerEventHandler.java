package com.fhs.weblog.handle;


import com.fhs.weblog.dto.LoggerEventDTO;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by kl on 2018/8/24.
 * Content :进程日志事件处理器
 */

public class LoggerEventHandler implements EventHandler<LoggerEventDTO> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onEvent(LoggerEventDTO stringEvent, long l, boolean b) {
        messagingTemplate.convertAndSend("/topic/pullLogger",stringEvent.getLog());
    }
}
