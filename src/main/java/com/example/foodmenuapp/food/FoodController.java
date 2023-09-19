package com.example.foodmenuapp.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping
    public Iterable<Food> getFoods() {
        return foodService.getFoods();
    }

    @GetMapping("/{id}")
    public Food getFood(@PathVariable String id) {
        return foodService.getFood(id);
    }

    @PutMapping("/{id}")
    public Food updateFood(@PathVariable String id, @RequestBody Food food) {
        if (!id.equals(food.id())) {
            throw new IllegalArgumentException("id does not match");
        }
        return foodService.updateFood(food);
    }

    @PostMapping
    public Food createFood(@RequestBody Food food) {
        return foodService.createFood(food);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
    }
}
