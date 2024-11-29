package com.project.oof.gpt.dto;

public record MessageDto (
        String message
){
    public static MessageDto of(String message) {
        return new MessageDto(message);
    }
}
