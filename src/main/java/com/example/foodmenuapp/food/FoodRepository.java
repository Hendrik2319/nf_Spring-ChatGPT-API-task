package com.example.foodmenuapp.food;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<Food, String> {
}
