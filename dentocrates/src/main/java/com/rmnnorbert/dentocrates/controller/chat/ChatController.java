package com.rmnnorbert.dentocrates.controller.chat;

import com.rmnnorbert.dentocrates.dto.chat.ChatDTO;
import com.rmnnorbert.dentocrates.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Tag(name = "Chat", description = "Chat management APIs")
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(
            summary = "Get the the chatbot provided response to the input",
            description = "Get the the chatbot provided response by the user provided input. The response is a String.",
            tags = { "Chat", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully get the chatbot response for the user provided input.",
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping
    public ResponseEntity<String> getChatBotResponse(@RequestBody ChatDTO input){
        return chatService.getChatResponse(input);
    }
}
