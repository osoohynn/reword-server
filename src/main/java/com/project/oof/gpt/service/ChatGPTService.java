package com.project.oof.gpt.service;

import org.springframework.stereotype.Service;


@Service
public interface ChatGPTService {

    String translateMessage(String userMessage);

    String refreshResult();
}
