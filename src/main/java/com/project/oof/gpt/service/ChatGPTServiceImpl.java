package com.project.oof.gpt.service;

import com.project.oof.gpt.config.ChatGPTConfig;
import com.project.oof.gpt.dto.request.TranslateRequest;
import com.project.oof.gpt.dto.response.MessageDto;
import com.project.oof.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;
    private final UserService userService;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Override
    public MessageDto translateMessage(TranslateRequest request) {
        UUID userId = request.userId();

        userService.addUser(userId);

        ChatHistoryService personalPromptList = userService.getChatHistory(userId);
        personalPromptList.clearChatHistory();

        String userMessage = request.message();
        String processedMessage = "'" + userMessage + "'를 번역해줘, 다른 말 없이 결과만 말해줘";

        personalPromptList.addMessage("user", processedMessage);

        return getAnswer(personalPromptList);
    }

    @Override
    public MessageDto refreshResult(UUID userId) {
        ChatHistoryService personalPromptList = userService.getChatHistory(userId);

        if (personalPromptList.answerHistory.isEmpty()) {
            throw new RuntimeException("입력된 데이터가 없습니다");
        }

        personalPromptList.addMessage("user", "좀만 다르게 번역해줘, 역시나 다른 말 빼고 결과만");

        return getAnswer(personalPromptList);
    }

    private MessageDto getAnswer(ChatHistoryService personalPromptList) {

        List<Map<String, String>> messages = personalPromptList.getChatHistory();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", messages
        );

        HttpHeaders headers = chatGPTConfig.httpHeaders();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = chatGPTConfig
                    .restTemplate()
                    .exchange(promptUrl, HttpMethod.POST, requestEntity, Map.class);

            String assistantMessage = ((List<Map<String, Object>>) response.getBody().get("choices"))
                    .stream()
                    .findFirst()
                    .map(choice -> (Map<String, Object>) choice.get("message"))
                    .map(message -> (String) message.get("content"))
                    .map(content -> content.replaceAll("^\"|\"$", ""))
                    .orElse("No response from assistant.");

            personalPromptList.addMessage("assistant", assistantMessage);
            personalPromptList.answerHistory.add(assistantMessage);

            return MessageDto.of(assistantMessage);

        } catch (Exception e) {
            return MessageDto.of("An error occurred while processing the response.");
        }
    }

    @Override
    public List<String> getAnswers(UUID userId) {
        ChatHistoryService personalPromptList = userService.getChatHistory(userId);

        return personalPromptList.answerHistory;
    }
}