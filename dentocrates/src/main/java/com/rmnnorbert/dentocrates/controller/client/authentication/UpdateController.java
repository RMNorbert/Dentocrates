package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.service.client.ClientUpdaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Tag(name = "Client update", description = "Client update management APIs")
@RestController
@RequestMapping("/update")
public class UpdateController {
    private final ClientUpdaterService clientUpdateService;
    @Autowired
    public UpdateController(ClientUpdaterService clientUpdateService) {
        this.clientUpdateService = clientUpdateService;
    }

    @Operation(
            summary = "Update a client password",
            description = "Update a client password with provided the ResetPasswordDTO. The response is a string indicating whether the password is successfully changed.",
            tags = { "Client update", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully change the client password.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Password change" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto
    ) {
        return clientUpdateService.resetPassword(dto);
    }

    @Operation(
            summary = "Update a client verified status",
            description = "Update a client verified status to verified. The response is a string indicating whether the verification is successfully.",
            tags = { "Client update", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully updated the client verified status.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Verification" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Verification code" + NOT_FOUND_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Verification code" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @Valid @RequestBody VerifyDTO dto
    ) {
        return clientUpdateService.verifyClient(dto);
    }

}
