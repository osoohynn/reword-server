package com.project.oof.gpt.dto.response;

public record MessageDto (
        String message
){
    public static MessageDto of(String message) {
        return new MessageDto(message);
    }
}
