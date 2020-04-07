package me.study.spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class NewBookRepository implements BookRepository {
}
