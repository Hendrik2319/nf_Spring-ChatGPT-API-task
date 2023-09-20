package com.example.foodmenuapp.chatgpt;

import java.util.List;

public record ChatGPTResponse (
        List<ChatGPTResponseMessage> choices
) {
    public record ChatGPTResponseMessage (
            String role,
            String content
    ) {}
}
