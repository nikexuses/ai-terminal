package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/terminal")
public class TestController {

    private final String API_KEY = "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";

    @GetMapping("/execute")
    public String execute(@RequestParam String input) {

        switch (input.toLowerCase()) {
            case "hello":
                return "hi bro 👋";
            case "help":
                return "Try anything. I am an AI terminal.";
            case "date":
                return java.time.LocalDateTime.now().toString();
            case "clear":
                return "CLEAR_SCREEN";
        }

        return callAI(input);
    }

    private String callAI(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String url = "https://openrouter.ai/api/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
            {
              "model": "deepseek/deepseek-chat",
              "messages": [
                {
                  "role": "system",
                  "content": "You are a terminal assistant. Give short, clean answers like a CLI."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ]
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}