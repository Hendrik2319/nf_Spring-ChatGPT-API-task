package com.example.foodmenuapp.food;

import lombok.With;

@With
public record Food(
        String id,
        String name,
        String category
) {
}
