package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.update.ResetDto;
import com.rmnnorbert.dentocrates.controller.dto.client.update.VerifyDto;
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
            @RequestBody AuthenticationRequest request
            ){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetDto dto
    ){
        return service.resetPassword(dto);
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @RequestBody VerifyDto dto
    ){
        return service.verifyClient(dto);
    }
}

