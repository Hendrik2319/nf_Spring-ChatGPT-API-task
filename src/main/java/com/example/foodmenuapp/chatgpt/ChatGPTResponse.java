package com.example.foodmenuapp.chatgpt;

import java.util.List;

public record ChatGPTResponse (
        List<MessageContainer> choices
) {
    public record MessageContainer(
            int index,
            Message message,
            String finish_reason
    ) {}
    public record Message(
            String role,
            String content
    ) {}
}
