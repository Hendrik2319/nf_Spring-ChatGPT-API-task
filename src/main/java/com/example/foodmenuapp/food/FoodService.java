package com.example.foodmenuapp.food;

import com.example.foodmenuapp.chatgpt.ChatGPTRequest;
import com.example.foodmenuapp.chatgpt.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final WebClient webClient;

    public FoodService(FoodRepository foodRepository,
                       @Value("${app.openai-api-key}") String openaiApiKey,
                       @Value("${app.openai-api-organization}") String openaiApiOrganization) {

        this.foodRepository = foodRepository;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .defaultHeader("OpenAI-Organization", openaiApiOrganization)
                .build();
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

        return askChatGPT(prompt);
    }

    private String askChatGPT(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(
                "gpt-3.5-turbo",
                List.of(
                        new ChatGPTRequest.Message(
                                "user",
                                prompt
                        )
                )
        );

        request.showContent(System.out, "request");

        ChatGPTResponse response = execFRequest(request);
        if (response==null) {
            System.out.println("response: <null>");
            return null;
        }

        response.showContent(System.out, "response");

        List<ChatGPTResponse.Choice> choices = response.choices();
        if (choices==null || choices.isEmpty()) return null;

        ChatGPTResponse.Choice firstChoice = choices.get(0);
        if (firstChoice==null) return null;

        ChatGPTResponse.Message message = firstChoice.message();
        if (message==null) return null;

        return message.content();
    }

    private ChatGPTResponse execFRequest(ChatGPTRequest request) {
        ResponseEntity<ChatGPTResponse> responseEntity = webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.empty())
                .toEntity(ChatGPTResponse.class)
                .block();

        if (responseEntity==null) return null;

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode.is4xxClientError()) return null;
        if (statusCode.is5xxServerError()) return null;

        return responseEntity.getBody();
    }
}
