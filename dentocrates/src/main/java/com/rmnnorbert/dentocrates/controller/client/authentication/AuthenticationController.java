package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import com.rmnnorbert.dentocrates.dto.client.verification.VerificationRequestDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
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

    @GetMapping("/oauth2/authorizationPageUrl/google")
    public String getAuthorizationPageUrl(){
        return service.getAuthorizationUrl();
    }

    @GetMapping("/login/oauth2/code/")
    public ResponseEntity<AuthenticationResponse> handleOauth2Redirect(@RequestParam String state,@RequestParam String code) {
        System.out.println(state);
        AuthenticationRequest request = service.registerWithOauth(state,code);
        System.out.println(code);
        return ResponseEntity.ok(service.authenticate(request));
    }

}
