package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.authentication.AuthenticationRequest;
import com.rmnnorbert.dentocrates.controller.dto.client.authentication.AuthenticationResponse;
import com.rmnnorbert.dentocrates.controller.dto.client.authentication.VerificationRequestDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.update.ResetDto;
import com.rmnnorbert.dentocrates.controller.dto.client.update.VerifyDto;
import com.rmnnorbert.dentocrates.security.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService service;
    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody CustomerRegisterDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/register/dentist")
    public ResponseEntity<AuthenticationResponse> registerDentist(
            @Valid @RequestBody DentistRegisterDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
           @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/request/authenticate")
    public ResponseEntity authenticateRequest(
            @RequestBody VerificationRequestDTO dto
    ) {
        return ResponseEntity.ok(service.sendAuthenticationCode(dto));
    }
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
           @Valid @RequestBody ResetDto dto
    ) {
        return service.resetPassword(dto);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
           @Valid @RequestBody VerifyDto dto
    ) {
        return service.verifyClient(dto);
    }

    @GetMapping("/oauth2/authorizationPageUrl/google")
    public String getAuthorizationPageUrl(){
        return service.getAuthorizationUrl();
    }
    @CrossOrigin("http://localhost:3000/")
    @GetMapping("login/oauth2/code/")
    public ResponseEntity<AuthenticationResponse> handleOauth2Redirect(@RequestParam String state,@RequestParam String code) {
        AuthenticationRequest request = service.registerWithOauth(state,code);
        return ResponseEntity.ok(service.authenticate(request));
    }

}
