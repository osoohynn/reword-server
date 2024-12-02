package com.project.oof.gpt.service;

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
//        removeUser();
        userChatHistories.putIfAbsent(userId, new ChatHistoryService());
    }

    public void removeUser() {
        userChatHistories.clear();
    }
}