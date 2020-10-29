package me.study.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final Logger logger;

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        while (true) {
            logger.write();
            Thread.sleep(1000);
        }
    }
}
