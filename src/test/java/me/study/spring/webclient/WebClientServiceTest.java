package me.study.spring.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WebClientServiceTest {

    @Autowired
    private WebClientService webClientService;

    @Test
    @DisplayName("")
    void sync() {
        long start = System.currentTimeMillis();
        List<String> results = webClientService.sync();
        long end = System.currentTimeMillis();

        results.forEach(System.out::println);
        System.out.println("소요 시간: " + (end - start) + "ms");
    }

    @Test
    @DisplayName("")
    void async() {
        long start = System.currentTimeMillis();
        List<String> results = webClientService.async();
        long end = System.currentTimeMillis();

        results.forEach(System.out::println);
        System.out.println("소요 시간: " + (end - start) + "ms");
    }
}
