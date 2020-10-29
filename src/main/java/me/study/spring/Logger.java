package me.study.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Future;

@Component
@Slf4j
public class Logger {

    public final static Queue<String> logQueue = new LinkedList<>();

    @Async
    public Future<Boolean> addQueue(String str) {

        log.info("addQueue: " + str);
        return new AsyncResult<>(logQueue.offer(str));
    }

    @Async
    public void write() {

        log.info("write");
        while (logQueue.size() > 0) {
            Optional.ofNullable(logQueue.poll()).ifPresent(this::fileWrite);
        }
    }

    private void fileWrite(String str) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.log", true))) {
            writer.append(LocalDateTime.now().toString()).append(" : ").append(str).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
