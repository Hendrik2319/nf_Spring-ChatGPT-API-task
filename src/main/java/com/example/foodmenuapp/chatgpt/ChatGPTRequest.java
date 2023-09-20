package com.example.foodmenuapp.chatgpt;

import java.util.List;

public record ChatGPTRequest (
        String model,
        List<ChatGPTRequestMessage> messages
) {
    public record ChatGPTRequestMessage (
            String role,
            String content
    ) {
        public ChatGPTRequestMessage(String content) {
            this( "user", content );
        }
    }
}
