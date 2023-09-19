package com.example.foodmenuapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FoodMenuAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodMenuAppApplication.class, args);
    }

    @Value("${app.openai-api-key}")
    private String openaiApiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();
    }

}
