package com.project.oof.service;

import com.project.oof.dto.ChatCompletionDto;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface ChatGPTService {

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}
