package org.rmnorbert.dentocrates.controller.client.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.update.ForgotPasswordDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerificationRequestDTO;
import org.rmnorbert.dentocrates.security.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;

@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService service;
    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }
    @Operation(
            summary = "Register a customer",
            description = "Register a customer by providing the necessary credentials. The response is a boolean indicating whether the entity is created.",
            tags = { "Authentication", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = CREATED_RESPONSE_CODE, description = "Successfully registered the customer.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/register/customer")
    public ResponseEntity<Boolean> register(
            @Valid @RequestBody CustomerRegisterDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @Operation(
            summary = "Register a dentist",
            description = "Register a dentist by providing the necessary credentials. The response is a boolean indicating whether the entity is created.",
            tags = { "Authentication", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = CREATED_RESPONSE_CODE, description = "Successfully registered the dentist.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/register/dentist")
    public ResponseEntity<Boolean> registerDentist(
            @Valid @RequestBody DentistRegisterDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @Operation(
            summary = "Authenticate a client",
            description = "Authenticate a client by providing the necessary credentials. The response is an AuthenticationResponse.",
            tags = { "Authentication", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successful client login.",
                    content = { @Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
           @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(
            summary = "Requesting authentication code",
            description = "Requesting to send an authentication code to client via email. The response is a boolean indicating whether the code is sent.",
            tags = { "Authentication", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Authentication code sent successfully.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/request/authenticate")
    public ResponseEntity<Boolean> authenticateRequest(
            @RequestBody VerificationRequestDTO dto
    ) {
        return ResponseEntity.ok(service.sendAuthenticationCode(dto));
    }

    @Operation(
            summary = "Requesting authorization page url",
            description = "Requesting authorization page url for OAuth2. The response is a string used during OAuth 2.0 authentication flow.",
            tags = { "Authentication", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Authentication code sent successfully.",
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT, description = INVALID_OAUTH2_CLIENT_REGISTRATION_EXCEPTION_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_OAUTH2_CLIENT_REGISTRATION_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/oauth2/authorizationPageUrl/google")
    public String getAuthorizationPageUrl(){
        return service.getAuthorizationUrl();
    }

    @Operation(
            summary = "Authenticate a client with OAuth2",
            description = "Authenticate a client with OAuth2. The response is an AuthenticationResponse.",
            tags = { "Authentication", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successful client login with OAuth2.",
                    content = { @Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/login/oauth2/code/")
    public ResponseEntity<AuthenticationResponse> handleOauth2Redirect(@RequestParam String state,@RequestParam String code) {
        AuthenticationRequest request = service.registerWithOauth(state,code);
        return ResponseEntity.ok(service.authenticate(request));
    }
    @Operation(
            summary = "Requesting authentication code to reset password",
            description = "Requesting to send an authentication code to reset password to the client via email. The response is a boolean indicating whether the code is sent.",
            tags = { "Authentication", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Authentication code sent successfully.",
                    content = { @Content(schema = @Schema(implementation = Boolean.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/reset/request")
    public ResponseEntity<Boolean> requestReset(@RequestBody ForgotPasswordDTO dto) {
        return ResponseEntity.ok(service.requestReset(dto));
    }
}
