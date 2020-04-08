package me.study.spring;

import me.study.out.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class StudySpringApplication {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
//        SpringApplication.run(StudySpringApplication.class, args);
        SpringApplication app = new SpringApplication(StudySpringApplication.class);
        app.addInitializers((ApplicationContextInitializer<GenericApplicationContext>) ctx -> {
            ctx.registerBean(MyService.class);
            ctx.registerBean(ApplicationRunner.class, () -> args1 -> System.out.println("Functional Bean Definition!"));
        });
        app.run(args);
    }

}
