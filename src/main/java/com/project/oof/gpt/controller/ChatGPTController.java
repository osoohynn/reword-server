package com.project.oof.gpt.controller;

import com.project.oof.gpt.dto.request.RefreshRequest;
import com.project.oof.gpt.dto.request.TranslateRequest;
import com.project.oof.gpt.dto.response.MessageDto;
import com.project.oof.gpt.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/translate")
    public ResponseEntity<MessageDto> selectPrompt(@RequestBody TranslateRequest request) {
        MessageDto result = chatGPTService.translateMessage(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageDto> refresh(@RequestBody RefreshRequest request) {
        MessageDto result = chatGPTService.refreshResult(request.userId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<String>> getAnswers(@RequestBody RefreshRequest request) {
        return new ResponseEntity<>(chatGPTService.getAnswers(request.userId()), HttpStatus.OK);
    }
}