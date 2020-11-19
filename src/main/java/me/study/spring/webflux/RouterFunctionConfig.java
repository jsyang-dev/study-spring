package me.study.spring.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    private final TacoRepository tacoRepo;

    public RouterFunctionConfig(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/design/taco"), this::recentTacos)
                .andRoute(POST("/design"), this::postTaco);

    }

    private Mono<ServerResponse> recentTacos(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepo.findAll().take(12), Taco.class);
    }

    private Mono<ServerResponse> postTaco(ServerRequest request) {
        Mono<Taco> tacoMono = request.bodyToMono(Taco.class);
        Mono<Taco> savedTaco = tacoRepo.saveAll(tacoMono).next();
        return ServerResponse.accepted().body(savedTaco, Taco.class);
    }
}
