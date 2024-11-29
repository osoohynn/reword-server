package com.project.oof.gpt.dto.request;

import java.util.UUID;

public record TranslateRequest(
        String message,
        UUID userId
) {
}
