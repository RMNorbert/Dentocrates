package org.rmnorbert.dentocrates.service.client;

import org.rmnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.verification.Verification;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.client.update.ResetPasswordDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerifyDTO;
import org.rmnorbert.dentocrates.service.client.communicationServices.VerificationService;
import org.rmnorbert.dentocrates.service.client.customer.CustomerService;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.SUCCESSFUL_REGISTER_RESPONSE_CONTENT;

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

    public ResponseEntity<String> resetPassword(ResetPasswordDTO dto) {
        boolean isResetRequestValid = verificationService.validate(dto.verificationCode(), dto.email());
        if(isResetRequestValid) {
            Verification verification = verificationService.getVerification(dto.verificationCode())
                .orElseThrow(() -> new NotFoundException("Verification"));

            updateClientPassword(verification, dto.password());
            return ResponseEntity.ok().body("Password change" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        } else {
            throw new InvalidCredentialException();
        }
    }

    public ResponseEntity<String> verifyClient(VerifyDTO dto) {
        Verification verification = verificationService.getVerification(dto.verificationCode())
                .orElseThrow(() -> new NotFoundException("Verification code"));

        if(verification.getRole().equals(Role.CUSTOMER)) {
            customerService.verifyCustomer(verification.getEmail());
        }
        else if (verification.getRole().equals(Role.DENTIST)) {
            dentistService.verifyDentist(verification.getEmail());
        }
        return ResponseEntity.ok().body("Verification" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
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
