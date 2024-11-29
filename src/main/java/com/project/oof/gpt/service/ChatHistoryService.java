package com.project.oof.gpt.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatHistoryService {

    private final List<Map<String, String>> chatHistory = new ArrayList<>();

    public void addMessage(String role, String content) {
        chatHistory.add(Map.of("role", role, "content", content));
    }

    public List<Map<String, String>> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    public void clearChatHistory() {
        chatHistory.clear();
        answerHistory.clear();
    }

    public List<String> answerHistory = new ArrayList<>();
}
