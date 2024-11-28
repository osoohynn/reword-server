package com.project.oof.gpt.service;

import com.project.oof.gpt.config.ChatGPTConfig;
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
    public String prompt(String userMessage) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");
        System.out.println(chatHistoryService.getChatHistory());

        chatHistoryService.addMessage("user", userMessage);

        List<Map<String, String>> messages = chatHistoryService.getChatHistory();
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", messages
        );

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();


        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, Map.class);

        // OpenAI API의 응답을 처리
        try {
            Map<String, Object> responseBody = response.getBody();
            String assistantMessage = ((List<Map<String, Object>>) responseBody.get("choices"))
                    .stream()
                    .findFirst()
                    .map(choice -> (Map<String, Object>) choice.get("message"))
                    .map(message -> (String) message.get("content"))
                    .orElse("No response from assistant.");

            System.out.println("Assistant response: " + assistantMessage);
            chatHistoryService.addMessage("assistant", assistantMessage);
            return assistantMessage;
        } catch (Exception e) {
            System.err.println("Error processing assistant response: " + e.getMessage());
            return "An error occurred while processing the response.";
        }
    }

}
