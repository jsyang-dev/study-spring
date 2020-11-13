package me.study.spring.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {

        return User.builder()
                .id(id)
                .name("User" + id)
                .build();
    }

    @GetMapping
    public Flux<User> getAsyncList() {

        return Flux.range(1, 10)
                .log()
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(this::getUser)
                .sorted((u1, u2) -> u2.getId() - u1.getId());
    }

    private Mono<User> getUser(int id) {

        log.info(String.format("Calling getUser(%d)", id));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return webClient.get()
                .uri("/user/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }
}
