package me.study.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReactorTest {

    @Test
    void reactorTest01() {

        Flux<String> flux = Flux.just("A");
        Flux<String> flux2 = flux.map(s -> "foo" + s);
        flux.subscribe(System.out::println);
        flux2.subscribe(System.out::println);
    }

    @Test
    void reactorTest02() throws InterruptedException {

        Flux.fromIterable(Arrays.asList("foo", "bar"))
                .doOnNext(System.out::println)
                .map(String::toUpperCase)
                .subscribe(System.out::println);

        Flux.error(new IllegalStateException())
                .doOnError(System.out::println)
                .subscribe();

        Flux.interval(Duration.ofMillis(100))
                .take(10)
                .subscribe(System.out::println);

        System.out.println("hello");

        Thread.sleep(5000);
    }

    @Test
    void reactorTest03() {

        Mono.just(1)
                .map(integer -> integer * 2)
                .or(Mono.just(100))
                .subscribe(System.out::println);

        System.out.println("hello");
    }

    @Test
    void reactorTest04() {

        Flux<String> flux = Flux.just("foo", "bar");

        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();

        Flux<User> userFlux = Flux.just(new User("swhite"), new User("jpinkman"));

        StepVerifier.create(userFlux)
                .assertNext(u -> assertThat(u.getUsername()).isEqualTo("swhite"))
                .assertNext(u -> assertThat(u.getUsername()).isEqualTo("jpinkman"))
                .verifyComplete();

        Flux<Long> take10 = Flux.interval(Duration.ofMillis(100))
                .take(10);

        StepVerifier.create(take10)
                .expectNextCount(10)
                .verifyComplete();
    }
}
