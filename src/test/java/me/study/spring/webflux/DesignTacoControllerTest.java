package me.study.spring.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

class DesignTacoControllerTest {

    @Test
    void shouldReturnRecentTacos() {

        Taco[] tacos = {
                testTaco(1L), testTaco(2L), testTaco(3L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);

        when(tacoRepository.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepository)).build();
        testClient.get().uri("/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                .jsonPath("$[0].name").isEqualTo(tacos[0].getName())
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                .jsonPath("$[1].name").isEqualTo(tacos[1].getName())
                .jsonPath("$[2].id").isEqualTo(tacos[2].getId().toString())
                .jsonPath("$[2].name").isEqualTo(tacos[2].getName());
    }

    @Test
    void shouldSaveATaco() {

        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);
        Mono<Taco> unsavedTaco = Mono.just(testTaco(1L));
        Taco savedTaco = testTaco(1L);
        Flux<Taco> savedTacoFlux = Flux.just(savedTaco);

        when(tacoRepository.saveAll(unsavedTaco)).thenReturn(savedTacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoController(tacoRepository)).build();
        testClient.post()
                .uri("/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTaco, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }

    @Test
    void webClientTest01() {

        Mono<Taco> mono = WebClient.create("http://localhost:8080")
                .get()
                .uri("/design/{id}", 0L)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new RuntimeException()))
                .bodyToMono(Taco.class);
    }

    @Test
    void webClientTest02() {

        Mono<Taco> mono = WebClient.create("http://localhost:8080")
                .get()
                .uri("/design/{id}", 0L)
                .exchange()
                .flatMap(cr -> {
                    if (cr.headers().header("TEST").contains(true)) {
                        return Mono.empty();
                    }
                    return Mono.just(cr);
                })
                .flatMap(cr -> cr.bodyToMono(Taco.class));
    }

    private Taco testTaco(Long number) {

        Taco taco = new Taco();
        taco.setId(number);
        taco.setName("Taco " + number);
        return taco;
    }
}