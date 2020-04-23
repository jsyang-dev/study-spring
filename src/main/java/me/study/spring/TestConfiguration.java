package me.study.spring;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
//@Profile("test")
public class TestConfiguration {

    @Bean
    public BookRepository bookRepository() {
        return new TestBookRepository();
    }

}
