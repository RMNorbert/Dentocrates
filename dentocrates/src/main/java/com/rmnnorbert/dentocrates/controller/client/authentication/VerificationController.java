package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordRequestDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerificationValidationDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationEmailService;
import com.rmnnorbert.dentocrates.service.client.verification.VerificationEntityService;
import com.rmnnorbert.dentocrates.service.client.verification.VerificationValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;
import static com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationEmailService.VERIFICATION_SUBJECT;

@Tag(name = "Client verification", description = "Client verification management APIs")
@RestController
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationEmailService verificationEmailService;
    private final VerificationEntityService verificationEntityService;
    private final VerificationValidationService verificationValidationService;
    @Autowired
    public VerificationController(VerificationEmailService verificationEmailService,
                                  VerificationEntityService verificationEntityService,
                                  VerificationValidationService verificationValidationService) {
        this.verificationEmailService = verificationEmailService;
        this.verificationEntityService = verificationEntityService;
        this.verificationValidationService = verificationValidationService;
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
        return ResponseEntity.ok(verificationValidationService.validate(dto.verificationCode(), dto.email()));
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
        return verificationEntityService.deleteVerification(verificationCode);
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
        return verificationEmailService.sendVerification(dto.email(),  dto.role(), "reset",true );
    }
}
