package com.project.oof.user.service;

import com.project.oof.gpt.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Map<UUID, ChatHistoryService> userChatHistories = new HashMap<>();

    public ChatHistoryService getChatHistory(UUID userId) {
        return userChatHistories.computeIfAbsent(userId, id -> new ChatHistoryService());
    }

    public void addUser(UUID userId) {
        userChatHistories.putIfAbsent(userId, new ChatHistoryService());
    }

}