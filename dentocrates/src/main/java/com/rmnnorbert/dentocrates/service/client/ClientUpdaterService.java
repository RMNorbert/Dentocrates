package com.rmnnorbert.dentocrates.service.client;

import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.dao.verification.Verification;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.dto.client.update.ResetPasswordDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationEmailService;
import com.rmnnorbert.dentocrates.service.client.customer.CustomerService;
import com.rmnnorbert.dentocrates.service.client.dentist.DentistService;
import com.rmnnorbert.dentocrates.service.client.verification.VerificationEntityService;
import com.rmnnorbert.dentocrates.service.client.verification.VerificationValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.SUCCESSFUL_REGISTER_RESPONSE_CONTENT;

@Service
public class ClientUpdaterService {
    private final VerificationEmailService verificationEmailService;
    private final VerificationEntityService verificationEntityService;
    private final VerificationValidationService verificationValidationService;

    private final CustomerService customerService;
    private final DentistService dentistService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ClientUpdaterService(VerificationEmailService verificationEmailService,
                                VerificationEntityService verificationEntityService,
                                VerificationValidationService verificationValidationService,
                                CustomerService customerService,
                                DentistService dentistService,
                                PasswordEncoder passwordEncoder) {
        this.verificationEmailService = verificationEmailService;
        this.verificationEntityService = verificationEntityService;
        this.verificationValidationService = verificationValidationService;
        this.customerService = customerService;
        this.dentistService = dentistService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Resets the password for a client based on the provided ResetPasswordDTO.
     *
     * @param dto The ResetPasswordDTO containing the verification code, email, and new password.
     * @return ResponseEntity with a success message if the password reset is successful.
     * @throws InvalidCredentialException If the reset request is not valid or the verification code is incorrect.
     */
    public ResponseEntity<String> resetPassword(ResetPasswordDTO dto) {
        boolean isResetRequestValid = verificationValidationService.validate(dto.verificationCode(), dto.email());
        if(isResetRequestValid) {
            Verification verification = verificationEntityService.getVerification(dto.verificationCode());

            updateClientPassword(verification, dto.password());
            return ResponseEntity.ok().body("Password change" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        } else {
            throw new InvalidCredentialException();
        }
    }

    /**
     * Verifies a client based on the provided verification code in the VerifyDTO.
     * Depending on the client's role, either a customer or dentist is verified.
     *
     * @param dto The VerifyDTO containing the verification code.
     * @return ResponseEntity with a success message if the client verification is successful.
     */
    public ResponseEntity<String> verifyClient(VerifyDTO dto) {
        Verification verification = verificationEntityService.getVerification(dto.verificationCode());

        if(verification.getRole().equals(Role.CUSTOMER)) {
            customerService.verifyCustomer(verification.getEmail());
        }
        else if (verification.getRole().equals(Role.DENTIST)) {
            dentistService.verifyDentist(verification.getEmail());
        }
        return ResponseEntity.ok().body("Verification" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }

    /**
     * Updates the password for a client based on the provided Verification and new password.
     * The password is encoded using the password encoder.
     *
     * @param verification The Verification object containing client information.
     * @param password     The new password to be set for the client.
     */
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
