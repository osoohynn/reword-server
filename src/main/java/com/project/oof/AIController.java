package com.project.oof;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class AIController {
    private final OpenAiChatModel openAiChatModel;
//    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    public AIController(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
//        this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestBody String message) {
//        Map<String, String> responses = new HashMap<>();

        return openAiChatModel.call(message);
//        responses.put("openai(chatGPT) 응답", openAiResponse);

//        String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
//        responses.put("vertexai(gemini) 응답", vertexAiGeminiResponse);
//        return responses;
    }
}