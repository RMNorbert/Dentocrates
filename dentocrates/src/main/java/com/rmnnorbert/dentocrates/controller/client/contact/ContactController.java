package com.rmnnorbert.dentocrates.controller.client.contact;

import com.rmnnorbert.dentocrates.dto.client.contact.ContactMessageDTO;
import com.rmnnorbert.dentocrates.service.client.communicationServices.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Tag(name = "Contact ", description = "Contact management APIs")
@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;
    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(
            summary = "Send the contact message to the support team",
            description = "Send the contact message to the support team. The response is a boolean indicating whether the message is sent.",
            tags = { "Contact", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "The message successfully sent.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping
    public ResponseEntity<Boolean> sendContactMessage(
            @RequestBody ContactMessageDTO messageDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.sendMessage(messageDTO));
    }
}
