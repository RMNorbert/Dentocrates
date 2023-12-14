package org.rmnorbert.dentocrates.controller.client.authentication;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.rmnorbert.dentocrates.dto.client.update.ResetPasswordRequestDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerificationValidationDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerifyDTO;
import org.rmnorbert.dentocrates.service.client.communicationServices.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;
import static org.rmnorbert.dentocrates.service.client.communicationServices.VerificationService.VERIFICATION_SUBJECT;

@Tag(name = "Client verification", description = "Client verification management APIs")
@RestController
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationService verificationService;
    @Autowired
    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }
    @Operation(
            summary = "Validate password reset request verification code",
            description = "Validate password reset request verification code by the provided VerificationValidationDTO. The response is a Boolean.",
            tags = { "Verification", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successful validation of verification code.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/{id}")
    public ResponseEntity<Boolean> validateResetVerification(@RequestBody VerificationValidationDTO dto) {
        return ResponseEntity.ok(verificationService.validate(dto.verificationCode(), dto.email()));
    }
    @Operation(
            summary = "Delete of the verification code",
            description = "Delete of the used verification code by the provided VerifyDTO. The response is a String.",
            tags = { "Verification", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = VERIFICATION_SUBJECT + DELETE_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = VERIFICATION_SUBJECT + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Verification code" + NOT_FOUND_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Verification code" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/")
    public ResponseEntity<String> deleteVerification(@RequestBody VerifyDTO verificationCode) {
        return verificationService.deleteVerification(verificationCode);
    }
    @Operation(
            summary = "Register and send verification code for the password reset",
            description = "Register and send verification code by the provided ResetPasswordLinkDTO. The response is a String.",
            tags = { "Verification", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successful register and send of the verification code.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = VERIFICATION_SUBJECT + SUCCESSFUL_REGISTER_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE_CODE, description = VERIFICATION_SUBJECT + " code already registered.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = VERIFICATION_SUBJECT + " code already registered."), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/reset/register")
    public ResponseEntity<String> registerReset(@RequestBody ResetPasswordRequestDTO dto) {
        return verificationService.sendVerification(dto.email(),  dto.role(), "reset",true );
    }
}
