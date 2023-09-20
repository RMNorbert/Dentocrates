package com.rmnnorbert.dentocrates.service.client;

import com.rmnnorbert.dentocrates.controller.dto.client.update.ResetDto;
import com.rmnnorbert.dentocrates.controller.dto.client.update.VerifyDto;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.verification.Verification;
import com.rmnnorbert.dentocrates.data.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientUpdaterService {
    private final VerificationService verificationService;
    private final CustomerService customerService;
    private final DentistService dentistService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ClientUpdaterService(VerificationService verificationService, CustomerService customerService, DentistService dentistService, PasswordEncoder passwordEncoder) {
        this.verificationService = verificationService;
        this.customerService = customerService;
        this.dentistService = dentistService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> resetPassword(ResetDto dto) {
        Verification verification = verificationService.getVerification(dto.verificationCode())
                .orElseThrow(() -> new NotFoundException("Verification"));

        if(verification.getEmail().equals(dto.email())) {
            updateClientPassword(verification, dto.password());
            return ResponseEntity.ok().body("Password changed successfully.");
        } else {
            throw new InvalidCredentialException();
        }
    }

    public ResponseEntity<String> verifyClient(VerifyDto dto) {
        Verification verification = verificationService.getVerification(dto.verificationCode())
                .orElseThrow(() -> new NotFoundException("Verification"));

        if(verification.getRole().equals(Role.CUSTOMER)) {
            customerService.verifyCustomer(verification.getEmail());
        }
        else if (verification.getRole().equals(Role.DENTIST)) {
            dentistService.verifyDentist(verification.getEmail());
        }
        return ResponseEntity.ok().body("Verification successful.");
    }
    private void updateClientPassword(Verification verification, String password ) {
        String newPassword = passwordEncoder.encode(password);

        if (verification.getRole().equals(Role.CUSTOMER)) {
            customerService.updateCustomerPassword(verification.getEmail(), newPassword);
        }

        else if (verification.getRole().equals(Role.DENTIST)) {
            dentistService.updateDentistPassword(verification.getEmail(), newPassword);
        }
    }

}
