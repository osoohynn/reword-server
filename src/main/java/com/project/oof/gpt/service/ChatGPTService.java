package com.project.oof.gpt.service;

import com.project.oof.gpt.dto.request.TranslateRequest;
import com.project.oof.gpt.dto.response.MessageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface ChatGPTService {

    MessageDto translateMessage(TranslateRequest request);

    MessageDto refreshResult(UUID userId);

    List<String> getAnswers(UUID userId);
}
