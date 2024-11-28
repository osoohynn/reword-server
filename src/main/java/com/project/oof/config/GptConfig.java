package com.project.oof.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GptConfig {

    public final static String MODEL = "gpt-3.5-turbo";
    public final static double TOP_P = 1.0;
    public final static int MAX_TOKEN = 2000;
    public final static double TEMPERATURE = 1.0;
    public final static Duration TIME_OUT = Duration.ofSeconds(300);

    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
