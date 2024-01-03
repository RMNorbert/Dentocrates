package com.rmnnorbert.dentocrates.service.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmnnorbert.dentocrates.dto.chat.ChatDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatService {
    private static final String PYTHON_SERVER_URL = System.getenv("CHAT_URL");
    private static final String ANSWER_RESPONSE_KEY = "answer";
    public ResponseEntity<String> getChatResponse(ChatDTO input){
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);
     HttpEntity<ChatDTO> requestEntity = new HttpEntity<>(input, headers);
     RestTemplate restTemplate = new RestTemplate();

        try {
        ResponseEntity<String> response = restTemplate.exchange(
                PYTHON_SERVER_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            String answer = responseMap.get(ANSWER_RESPONSE_KEY);

            return new ResponseEntity<>(answer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: " + response.getStatusCodeValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } catch (Exception e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
 }
}
