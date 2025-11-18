package com.trivia.triviaapplication.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.trivia.triviaapplication.model")
@EnableJpaRepositories(basePackages = "com.trivia.triviaapplication.repository")
public class TestApplication {
}
