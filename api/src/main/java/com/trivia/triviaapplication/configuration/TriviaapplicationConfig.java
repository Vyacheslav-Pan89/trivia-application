package com.trivia.triviaapplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TriviaapplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
