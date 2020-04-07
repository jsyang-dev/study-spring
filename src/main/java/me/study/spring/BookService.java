package me.study.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    public void setUp() {
        System.out.println(bookRepository.getClass());
    }

}
