package me.study.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements ApplicationRunner {

    private final Logger logger;

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {

//        while (true) {
//            log.info("queue.size(): " + Logger.logQueue.size());
//            logger.write();
//            Thread.sleep(5000);
//        }
    }
}
