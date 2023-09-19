package com.example.foodmenuapp.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final WebClient webClient;

    public Iterable<Food> getFoods() {
        return foodRepository.findAll();
    }

    public Food getFood(String id) {
        return foodRepository.findById(id).orElseThrow();
    }

    public Food createFood(Food food) {
        if (food.category() == null) {
            food = food.withCategory(calculateCategory(food.name()));
        }
        return foodRepository.save(food);
    }

    private String calculateCategory(String name) {
        return webClient.post()
                .bodyValue(new ChatGPTRequest("categories: [vegan, vegetarisch, Hausmannskost, usw...]; output: best fitting category as string; outputformat json: {\"category\": \"chosen category\"}; input:  " + name))
                .retrieve()
                .bodyToMono(ChatGPTResponse.class)
                .map(ChatGPTResponse::text)
                .block();
    }

    public Food updateFood(Food food) {
        return foodRepository.save(food);
    }

    public void deleteFood(String id) {
        foodRepository.deleteById(id);
    }
}
