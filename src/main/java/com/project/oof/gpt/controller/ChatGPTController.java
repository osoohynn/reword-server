package com.project.oof.gpt.controller;

import com.project.oof.gpt.service.ChatGPTService;
import com.project.oof.storage.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChatGPTController {
    private final ChatHistoryService chatHistoryService;

    private final ChatGPTService chatGPTService;

    @PostMapping("/prompt")
    public ResponseEntity<String> selectPrompt(@RequestBody String message) {
        String result = chatGPTService.prompt(message);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearChatHistory() {
        chatHistoryService.clearChatHistory(); // 대화 기록 초기화
        return ResponseEntity.ok("Chat history cleared.");
    }

}