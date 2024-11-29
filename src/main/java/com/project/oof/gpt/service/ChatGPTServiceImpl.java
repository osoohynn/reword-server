package com.project.oof.gpt.service;

import com.project.oof.gpt.config.ChatGPTConfig;
import com.project.oof.gpt.dto.MessageDto;
import com.project.oof.storage.service.ChatHistoryService;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;
    private final ChatHistoryService chatHistoryService;

    @Value("${openai.url.prompt}")
    private String promptUrl;


    @Override
    public MessageDto translateMessage(String userMessage) {
        chatHistoryService.clearChatHistory();

        String processedMessage = "'" + userMessage + "' 를 번역해줘, 다른 말 없이 결과만 말해줘";

        chatHistoryService.addMessage("user", processedMessage);

        return getAnswer();
    }

    @Override
    public MessageDto refreshResult() {
        if (chatHistoryService.getChatHistory().isEmpty()) {
            throw new RuntimeException("입력된 데이터가 없습니다");
        }

        chatHistoryService.addMessage("user", "좀만 다르게 번역해줘");

        return getAnswer();
    }

    private MessageDto getAnswer() {
        List<Map<String, String>> messages = chatHistoryService.getChatHistory();
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", messages
        );

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, Map.class);

        try {
            String assistantMessage = ((List<Map<String, Object>>) response.getBody().get("choices"))
                    .stream()
                    .findFirst()
                    .map(choice -> (Map<String, Object>) choice.get("message"))
                    .map(message -> (String) message.get("content"))
                    .map(content -> content.replaceAll("^\"|\"$", "")) // 문자열 양쪽의 따옴표 제거
                    .orElse("No response from assistant.");

            // 대화 기록 저장
            chatHistoryService.addMessage("assistant", assistantMessage);
            chatHistoryService.answerHistory.add(assistantMessage);

            // 메시지 DTO 반환
            return MessageDto.of(assistantMessage);
        } catch (Exception e) {
            return MessageDto.of("An error occurred while processing the response.");
        }
    }

    @Override
    public List<String> getAnswers() {
        return chatHistoryService.answerHistory;
    }
}