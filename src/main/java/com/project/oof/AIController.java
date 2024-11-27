package com.project.oof;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
public class AIController {
    private final OpenAiChatModel openAiChatModel;

    public AIController(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestBody String message) {

        return openAiChatModel.call(message);
    }
}