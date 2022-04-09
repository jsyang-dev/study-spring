package me.study.spring.webclient;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebClientService {

    public List<String> sync() {
        List<String> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String result = getWebClient().get()
                    .uri("/hello")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .log()
                    .toStream()
                    .findFirst()
                    .orElse("");
            results.add(result);
        }

        return results;
    }

    public List<String> async() {
        List<String> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            getWebClient().get()
                    .uri("/hello")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .subscribe(results::add);
        }

        return results;
    }

    @NonNull
    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
