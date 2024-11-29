package com.project.oof.gpt.service;

import com.project.oof.gpt.dto.MessageDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ChatGPTService {

    MessageDto translateMessage(String userMessage);

    MessageDto refreshResult();

    List<String> getAnswers();
}
