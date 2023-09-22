package com.example.foodmenuapp.food;

import com.example.foodmenuapp.chatgpt.ChatGptService;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final ChatGptService chatGptService;

    public FoodService(
            FoodRepository foodRepository,
            ChatGptService chatGptService
    ) {
        this.foodRepository = foodRepository;
        this.chatGptService = chatGptService;
    }

    public Iterable<Food> getFoods() {
        return foodRepository.findAll();
    }

    public Food getFood(String id) {
        return foodRepository.findById(id).orElseThrow();
    }

    public Food createFood(Food food) {
        if (food.category()==null)
            food = food.withCategory(askChatGPTForCategory(food));
        return foodRepository.save(food);
    }

    public Food updateFood(Food food) {
        return foodRepository.save(food);
    }

    public void deleteFood(String id) {
        foodRepository.deleteById(id);
    }

    private String askChatGPTForCategory(Food food) {
        if (food.name()==null)
            return null;

        //String prompt = "Give me a category name for %s".formatted(food.name());
        String prompt =
                "categories: [vegan, vegetarisch, Hausmannskost, Asiatisch, Nudel, Fisch, Spanisch, Deutsch, Mediterran];" +
                        " output: best fitting category as string;" +
                        " outputformat: ${category};" +
                        " input: "+food.name();

        return chatGptService.askChatGPT(prompt);
    }
}
