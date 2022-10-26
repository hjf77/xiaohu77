package com.fhs.basics.api.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.fhs.basics.api.context.BisLoggerContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Date;
import java.text.DateFormat;

public class SysLogLogbackFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //前端请求才进行日志记录
        if(servletRequestAttributes != null && BisLoggerContext.isNeedLogger()){
            BisLoggerContext.getSysLogBuilder().append(DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp()))  + " "
                    + event.getLoggerName() + " " + event.getLevel().levelStr + " " + event.getMessage() +  "\n");
        }
        return FilterReply.ACCEPT;
    }
}