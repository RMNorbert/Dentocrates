package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordLinkDTO;
import com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationService;
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

    @DeleteMapping("/")
    public ResponseEntity<String> deleteVerification(@RequestBody String verificationCode) {
        return verificationService.deleteVerification(verificationCode);
    }
    @PostMapping("/reset/register")
    public ResponseEntity<String> registerReset(@RequestBody ResetPasswordLinkDTO dto) {
        return verificationService.sendVerification(dto.email(),  dto.role(), "reset",true );
    }
}
