package me.study.spring.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static reactor.core.publisher.Mono.when;

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

    private Taco testTaco(Long number) {

        Taco taco = new Taco();
        taco.setId(number);
        taco.setName("Taco " + number);
        return taco;
    }
}