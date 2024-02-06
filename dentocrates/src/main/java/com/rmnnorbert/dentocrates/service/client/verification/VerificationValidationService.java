package com.rmnnorbert.dentocrates.service.client.verification;

import com.rmnnorbert.dentocrates.repository.client.verification.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationValidationService {
    private final VerificationRepository verificationRepository;
    @Autowired
    public VerificationValidationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    /**
     * Validates a verification by checking if an entity with the given verification code and email exists.
     *
     * @param verificationCode The unique code associated with the verification entity.
     * @param email The email associated with the verification entity.
     * @return True if a matching verification entity is found; false otherwise.
     */
    public boolean validate(String verificationCode, String email) {
        return verificationRepository.findByVerificationCodeAndEmail(verificationCode, email).isPresent();
    }
}
