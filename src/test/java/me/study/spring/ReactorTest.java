package me.study.spring;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.scheduler.Schedulers.parallel;

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
                .assertNext(u -> assertThat(u.getName()).isEqualTo("swhite"))
                .assertNext(u -> assertThat(u.getName()).isEqualTo("jpinkman"))
                .verifyComplete();

        Flux<Long> take10 = Flux.interval(Duration.ofMillis(100))
                .take(10);

        StepVerifier.create(take10)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void reactorTest05() {

        Mono<User> mono = Mono.just(new User("hello")).map(u -> new User(u.getName().toUpperCase()));

        StepVerifier.create(mono)
                .assertNext(u -> assertThat(u.getName()).isEqualTo("HELLO"))
                .verifyComplete();
    }

    @Test
    void reactorTest06() {

        Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .window(3)
                .flatMap(l -> l.map(this::toUpperCase).subscribeOn(parallel()))
                .doOnNext(System.out::println)
                .blockLast();

        Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .window(3)
                .concatMap(l -> l.map(this::toUpperCase).subscribeOn(parallel()))
                .doOnNext(System.out::println)
                .blockLast();

        Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .window(3)
                .flatMapSequential(l -> l.map(this::toUpperCase).subscribeOn(parallel()))
                .doOnNext(System.out::println)
                .blockLast();
    }

    private List<String> toUpperCase(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(s.toUpperCase(), Thread.currentThread().getName());
    }

    @Test
    void reactorTest07() {

        Flux<Long> flux1 = Flux.interval(Duration.ofMillis(100)).take(10);
        Flux<Long> flux2 = Flux.just(100L, 101L, 102L);

        flux1.mergeWith(flux2)
                .doOnNext(System.out::println)
                .blockLast();

        flux1.concatWith(flux2)
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    void reactorTest08() {

        Mono<Long> mono1 = Mono.just(1L);
        Mono<Long> mono2 = Mono.just(2L);

        Flux.concat(mono1, mono2)
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    void reactorTest09() {

        Flux<Integer> flux = Flux.range(0, 100)
                .doOnSubscribe(s -> System.out.println("Start"))
                .doOnNext(System.out::println)
                .doOnComplete(() -> System.out.println("The end!"))
                .doOnCancel(() -> System.out.println("Canceled!"))
                .log();

        StepVerifier.create(flux, 1)
                .expectNext(0)
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    void reactorTest10() {

        Flux.range(1, 100)
                .log()
                .doOnNext(System.out::println)
                .subscribe(new Subscriber<>() {

                    private Subscription subscription;
                    private int count;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.subscription.request(10);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        count++;
                        if (count % 10 == 0) {
                            this.subscription.request(10);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    void reactorTest11() {

        Mono<Object> mono = Mono.error(new RuntimeException());
        mono.log().onErrorReturn(2).doOnNext(System.out::println).subscribe();
        mono.log().onErrorResume(e -> Mono.just(2)).doOnNext(System.out::println).subscribe();

        Mono.just("hello").log()
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .onErrorReturn(-1)
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    void reactorTest12() {

        String[] fruits = new String[]{
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        Flux<String> flux = Flux.fromArray(fruits).log();

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    void reactorTest13() {

        Flowable<Integer> flowable = Flowable.fromPublisher(Flux.just(1));
        Flux.from(flowable);

        Observable<Integer> observable = Observable.just(1);
        Flux<Integer> flux = Flux.from(observable.toFlowable(BackpressureStrategy.BUFFER));
        Observable.fromPublisher(flux);

        Single<Integer> single = Single.just(2);
        Mono<Integer> mono = Mono.from(single.toFlowable());
        Single.fromPublisher(mono);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");
        future.thenApply(String::toUpperCase);
        Mono<String> monoFromFuture = Mono.fromFuture(future);
        monoFromFuture.toFuture();
    }

    @Test
    void reactorTest14() {

        Flux<Integer> flux1 = Flux.range(1, 10);
        Flux<Integer> flux2 = Flux.range(11, 20);
        Flux<Integer> flux3 = Flux.range(21, 30);

        Flux.zip(flux1, flux2, flux3)
                .log()
                .map(t -> t.getT1() + t.getT2() + t.getT3())
                .doOnNext(System.out::println)
                .subscribe();

        Flux.first(flux1, flux2, flux3)
                .log()
                .doOnNext(System.out::println)
                .subscribe();

        Mono<Integer> mono1 = Mono.just(1);
        Mono<Integer> mono2 = Mono.just(2);
        Mono<Integer> mono3 = Mono.just(3);

        Mono.first(mono1, mono2, mono3)
                .log()
                .doOnNext(System.out::println)
                .subscribe();

        flux1.then()
                .log()
                .doOnNext(System.out::println)
                .subscribe();

        Mono.justOrEmpty(null)
                .defaultIfEmpty(100)
                .log()
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    void reactorTest15() {

        Integer num = Mono.just(1).block();
        System.out.println(num);

        Iterable<Integer> iterable = Flux.range(1, 10)
                .toIterable();
        for (Integer integer : iterable) {
            System.out.println(integer);
        }
    }

    @Test
    void reactorTest16() {

        Flux<Integer> flux1 = Flux.range(1, 10);
        Flux<Integer> flux2 = Flux.range(11, 20)
                .delayElements(Duration.ofMillis(1000));

        Flux<Tuple2<Integer, Integer>> zippedFlux = Flux.zip(flux1, flux2).log();

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p -> p.getT1() == 1 && p.getT2() == 11)
                .expectNextMatches(p -> p.getT1() == 2 && p.getT2() == 12)
                .expectNextMatches(p -> p.getT1() == 3 && p.getT2() == 13)
                .thenCancel()
                .verify();
    }

    @Test
    void reactorTest17() {

        Flux<Integer> flux = Flux.just("1 2", "3 4", "5 6").log()
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return Integer.parseInt(split[0]) + Integer.parseInt(split[1]);
                        })
                        .subscribeOn(parallel())
                );

        StepVerifier.create(flux)
                .expectNextMatches(p -> p == 3)
                .expectNextMatches(p -> p == 7)
                .expectNextMatches(p -> p == 11)
                .verifyComplete();
    }

    @Test
    void reactorTest18() {

        UserRepository userRepository = new UserRepository();

        Flux.defer(() -> Flux.fromIterable(userRepository.findAll()))
                .doOnNext(System.out::println)
                .subscribeOn(Schedulers.elastic())
                .subscribe();
    }

    @Test
    void reactorTest19() {

        Flux<Integer> flux = Flux.range(1, 9).log();
        Flux<List<Integer>> bufferedFlux = flux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList(1, 2, 3))
                .expectNext(Arrays.asList(4, 5, 6))
                .expectNext(Arrays.asList(7, 8, 9))
                .verifyComplete();

        Flux.range(1, 9)
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y -> y + 10)
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();
    }

    static public class User {

        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static public class UserRepository {
        public List<User> findAll() {
            return List.of(new User("yang"), new User("lee"));
        }
    }
}
