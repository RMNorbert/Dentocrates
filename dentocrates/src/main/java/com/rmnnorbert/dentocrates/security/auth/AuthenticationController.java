package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody CustomerRegisterDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/register/dentist")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody DentistRegisterDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Throwable {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
