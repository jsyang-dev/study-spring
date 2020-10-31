package me.study.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements ApplicationRunner {

    private final Logger logger;

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {

        Flux<String> flux = Flux.just("A");
        Flux<String> flux2 = flux.map(s -> "foo" + s);
        flux.subscribe(System.out::println);
        flux2.subscribe(System.out::println);

        while (true) {
            log.info("queue.size(): " + Logger.logQueue.size());
            logger.write();
            Thread.sleep(5000);
        }
    }
}
