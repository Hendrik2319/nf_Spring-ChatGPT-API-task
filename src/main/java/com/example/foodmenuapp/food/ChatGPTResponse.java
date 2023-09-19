package com.example.foodmenuapp.food;

import java.util.List;

record ChatGPTResponse(
        List<ChatGPTChoice> choices
) {
    public String text() {
        return choices.get(0).message().content();
    }
}