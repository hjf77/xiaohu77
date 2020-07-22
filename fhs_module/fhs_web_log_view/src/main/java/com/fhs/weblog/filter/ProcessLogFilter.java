package com.fhs.weblog.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.fhs.weblog.dto.LoggerMessageDTO;
import com.fhs.weblog.queue.LoggerDisruptorQueue;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;

@Component
public class ProcessLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        LoggerMessageDTO loggerMessage = new LoggerMessageDTO(
                event.getMessage()
                , DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                event.getThreadName(),
                event.getLoggerName(),
                event.getLevel().levelStr
        );
        LoggerDisruptorQueue.publishEvent(loggerMessage);
        return FilterReply.ACCEPT;
    }
}