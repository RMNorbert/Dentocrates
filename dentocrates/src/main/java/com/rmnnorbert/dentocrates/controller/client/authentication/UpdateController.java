package com.rmnnorbert.dentocrates.controller.client.authentication;

import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.service.client.ClientUpdaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class UpdateController {
    private final ClientUpdaterService clientUpdateService;
    @Autowired
    public UpdateController(ClientUpdaterService clientUpdateService) {
        this.clientUpdateService = clientUpdateService;
    }

    @PostMapping("/password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto
    ) {
        return clientUpdateService.resetPassword(dto);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @Valid @RequestBody VerifyDTO dto
    ) {
        return clientUpdateService.verifyClient(dto);
    }

}
