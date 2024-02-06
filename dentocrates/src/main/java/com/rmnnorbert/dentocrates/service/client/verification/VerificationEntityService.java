package com.rmnnorbert.dentocrates.service.client.verification;

import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.verification.Verification;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.repository.client.verification.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.DELETE_RESPONSE_CONTENT;
import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.SUCCESSFUL_REGISTER_RESPONSE_CONTENT;
import static com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationEmailService.VERIFICATION_SUBJECT;

@Service
public class VerificationEntityService {
    private final VerificationRepository verificationRepository;
    @Autowired
    public VerificationEntityService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }
    public Verification getVerification(String verificationCode) {
        return verificationRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new NotFoundException("Verification code"));
    }
    public ResponseEntity<String> deleteVerification(VerifyDTO dto) {
        Verification verification = getVerification(dto.verificationCode());
        verificationRepository.delete(verification);
        return ResponseEntity.ok(VERIFICATION_SUBJECT + DELETE_RESPONSE_CONTENT);
    }

    /**
     * Registers a verification code in the system.
     *
     * If the verification code is not already registered, a new Verification entity is created
     * with the specified email, role, and code, and it is saved to the verification repository.
     *
     * @param email The email address associated with the verification.
     * @param role The role (e.g., "CUSTOMER", "DENTIST") for which the verification is requested.
     * @param code The unique verification code to be registered.
     * @return ResponseEntity indicating the registration status.
     *         If successful, returns an OK response with a success message.
     *         If the code is already registered, returns a bad request response with an error message.
     */
    public ResponseEntity<String> registerVerification(String email, Role role, String code) {
        Optional<Verification> searchedVerification = verificationRepository.findByVerificationCode(code);

        if(searchedVerification.isEmpty()) {
            Verification verification = Verification.of(email,role,code);

            verificationRepository.save(verification);
            return ResponseEntity.ok(VERIFICATION_SUBJECT+ SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body( VERIFICATION_SUBJECT + " code already registered.");
    }
}
