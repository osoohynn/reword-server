package com.project.oof.storage.service;

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
        return new ArrayList<>(chatHistory); // 복사본 반환
    }

    // 대화 기록 초기화 (옵션)
    public void clearChatHistory() {
        chatHistory.clear();
    }
}
