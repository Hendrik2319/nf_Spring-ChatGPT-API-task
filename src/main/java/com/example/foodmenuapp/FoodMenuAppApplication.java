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
}
