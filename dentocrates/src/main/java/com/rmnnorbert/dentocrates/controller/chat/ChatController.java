package com.rmnnorbert.dentocrates.controller.chat;

import com.rmnnorbert.dentocrates.dto.chat.ChatDTO;
import com.rmnnorbert.dentocrates.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping
    public ResponseEntity<String> getCustomerAppointments(@RequestBody ChatDTO input){
        return chatService.getChatResponse(input);
    }
}
