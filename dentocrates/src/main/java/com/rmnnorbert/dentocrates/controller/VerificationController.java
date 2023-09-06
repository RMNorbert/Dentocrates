package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.client.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationService verificationService;
    @Autowired
    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }
    @GetMapping("/{id}")
    public boolean validateVerification(@PathVariable String id) {
        return verificationService.validate(id);
    }
    @DeleteMapping("/")
    public ResponseEntity<String> deleteVerification(@RequestBody String verificationCode) {
        return verificationService.deleteVerification(verificationCode);
    }
    @PostMapping("/reset/register")
    public ResponseEntity<String> registerReset(@RequestBody String email, String role) {
        return verificationService.sendVerification(email,  role, "reset",true );
    }
}
