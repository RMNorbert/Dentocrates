package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordLinkDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerificationValidationDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
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
    @PostMapping("/{id}")
    public ResponseEntity<Boolean> validateResetVerification(@RequestBody VerificationValidationDTO dto) {
        return ResponseEntity.ok(verificationService.validate(dto.verificationCode(), dto.email()));
    }
    @DeleteMapping("/")
    public ResponseEntity<String> deleteVerification(@RequestBody VerifyDTO verificationCode) {
        return verificationService.deleteVerification(verificationCode);
    }
    @PostMapping("/reset/register")
    public ResponseEntity<String> registerReset(@RequestBody ResetPasswordLinkDTO dto) {
        return verificationService.sendVerification(dto.email(),  dto.role(), "reset",true );
    }
}
