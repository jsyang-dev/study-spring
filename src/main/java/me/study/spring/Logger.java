package me.study.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@Component
@Slf4j
public class Logger {

    private Queue<String> logQueue = new LinkedList<>();

    @Async
    public Boolean addQueue(String str) {
//        log.info("addQueue: " + str);
        return logQueue.offer(str);
    }

    @Async
    public void write() {
//        log.info("write: " + logQueue.toString());
        while (logQueue.size() > 0) {
            Optional.ofNullable(logQueue.poll()).ifPresent(log::info);
        }
    }
}
