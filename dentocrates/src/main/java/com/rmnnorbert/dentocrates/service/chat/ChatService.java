package com.rmnnorbert.dentocrates.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmnnorbert.dentocrates.dto.chat.ChatDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatService {
    /**
     * The URL of the Chat server hosting the chatbot service.
     * This URL is used for sending inputs to the chatbot and receiving responses.
     */
    private static final String CHATBOT_SERVER_URL = System.getenv("CHAT_URL");
    /** Key used to extract the answer from a chatbot response. */
    private static final String ANSWER_RESPONSE_KEY = "answer";

    /**
     * Sends a chatbot request using the provided ChatDTO, retrieves the chatbot response,
     * and processes the response to extract the answer.
     *
     * @param input The ChatDTO representing the input for the chatbot.
     * @return ResponseEntity<String> containing the extracted answer or an error message,
     *         along with the corresponding HTTP status.
     */
    public ResponseEntity<String> getChatResponse(ChatDTO input){
        HttpEntity<ChatDTO> requestEntity = createJsonHttpEntity(input);

        try {
            ResponseEntity<String> response = sendChatbotRequest(requestEntity);

            if (response.getStatusCode() == HttpStatus.OK) {
                String answer = getValueFromResponse(response, ANSWER_RESPONSE_KEY);
                return new ResponseEntity<>(answer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error: " + response.getStatusCodeValue(),
                                            HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates an HTTP entity with JSON content type for the provided ChatDTO.
     *
     * @param input The ChatDTO to be wrapped in the HTTP entity.
     * @return An HttpEntity<ChatDTO> with JSON content type.
     */
    private HttpEntity<ChatDTO> createJsonHttpEntity(ChatDTO input) {
         HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(input, headers);
    }

    /**
     * Sends a chatbot request using the provided HttpEntity<ChatDTO> and retrieves the corresponding response.
     *
     * @param requestEntity The HttpEntity<ChatDTO> representing the chatbot request.
     * @return The ResponseEntity<String> containing the chatbot response.
     */
    private ResponseEntity<String> sendChatbotRequest(HttpEntity<ChatDTO> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                CHATBOT_SERVER_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    /**
     * Extracts the value associated with the specified key from the given ResponseEntity<String> containing a
     * chatbot response.
     *
     * @param response The ResponseEntity<String> representing the chatbot response.
     * @param key      The key to extract the value from the response.
     * @return The extracted value associated with the specified key from the response.
     * @throws JsonProcessingException If there is an issue processing the JSON content in the response.
     */
    private String getValueFromResponse(ResponseEntity<String> response, String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        return responseMap.get(key);
    }
}
