package com.fhs.weblog.queue;

import com.fhs.weblog.disruptor.*;
import com.fhs.weblog.dto.FileLoggerEventDTO;
import com.fhs.weblog.dto.LoggerEventDTO;
import com.fhs.weblog.dto.LoggerMessageDTO;
import com.fhs.weblog.handle.LoggerEventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kl on 2018/8/24.
 * Content :Disruptor 环形队列
 */
@Component
public class LoggerDisruptorQueue {

    private Executor executor = Executors.newCachedThreadPool();

    // The factory for the event
    private LoggerEventFactory factory = new LoggerEventFactory();

    private FileLoggerEventFactory fileLoggerEventFactory = new FileLoggerEventFactory();

    // Specify the size of the ring buffer, must be power of 2.
    private int bufferSize = 2 * 1024;

    // Construct the Disruptor
    private Disruptor<LoggerEventDTO> disruptor = new Disruptor<>(factory, bufferSize, executor);
    ;

    private Disruptor<FileLoggerEventDTO> fileLoggerEventDisruptor = new Disruptor<>(fileLoggerEventFactory, bufferSize, executor);
    ;

    private static RingBuffer<LoggerEventDTO> ringBuffer;

    private static RingBuffer<FileLoggerEventDTO> fileLoggerEventRingBuffer;

    @Autowired
    LoggerDisruptorQueue(LoggerEventHandler eventHandler) {
        disruptor.handleEventsWith(eventHandler);
        this.ringBuffer = disruptor.getRingBuffer();
        this.fileLoggerEventRingBuffer = fileLoggerEventDisruptor.getRingBuffer();
        disruptor.start();
        fileLoggerEventDisruptor.start();
    }

    public static void publishEvent(LoggerMessageDTO log) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            LoggerEventDTO event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.setLog(log);  // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }


}
