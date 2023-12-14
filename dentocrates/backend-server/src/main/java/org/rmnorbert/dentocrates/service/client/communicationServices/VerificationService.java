package org.rmnorbert.dentocrates.service.client.communicationServices;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.verification.Verification;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.client.verification.VerifyDTO;
import org.rmnorbert.dentocrates.repository.client.verification.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class VerificationService {
    private final GMailerService gMailerService;
    private final VerificationRepository verificationRepository;
    public final static String VERIFICATION_SUBJECT = "Verification";
    @Autowired
    public VerificationService(GMailerService gMailerService, VerificationRepository verificationRepository) {
        this.gMailerService = gMailerService;
        this.verificationRepository = verificationRepository;
    }

    public Optional<Verification> getVerification(String verificationCode) {
        return verificationRepository.findByVerificationCode(verificationCode);
    }
    public boolean validate(String verificationCode, String email) {
        return verificationRepository.findByVerificationCodeAndEmail(verificationCode, email).isPresent();
    }
    public ResponseEntity<String> sendVerification(String email, String role, String action, boolean reset) {
        String verificationCode = UUID.randomUUID().toString();
        String verificationMessage = "Verify " + action + " by login and to your profile page and send the verification code : ";
        Role roleAsEnum = Role.valueOf(role);
        ResponseEntity<String> response = registerVerification(email, roleAsEnum, verificationCode);

        if(reset) {
            verificationMessage += "reset password with: ";
        }
        verificationMessage += verificationCode;

        gMailerService.sendMail(email,VERIFICATION_SUBJECT,verificationMessage,GMailerService.BASE_URL);
        return response;
    }
    public String sendAuthenticationCode(String email, String role) {
        String authenticationCode = UUID.randomUUID().toString();
        String verificationMessage = "Authentication code to login : " + authenticationCode;
        String subject = "Dentocrates: Authentication code";
        String noLink = "";
        Role roleAsEnum = Role.valueOf(role);

        registerVerification(email, roleAsEnum, authenticationCode);

        gMailerService.sendMail(email,subject,verificationMessage,noLink);
        return authenticationCode;
    }
    public ResponseEntity<String> deleteVerification(VerifyDTO dto) {
        try {
            Verification verification = getVerification(dto.verificationCode())
                    .orElseThrow(() -> new NotFoundException("Verification"));

            verificationRepository.delete(verification);
            return ResponseEntity.ok(VERIFICATION_SUBJECT + DELETE_RESPONSE_CONTENT);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(INTERNAL_SERVER_ERROR_RESPONSE_CONTENT);
        }
    }

    private ResponseEntity<String> registerVerification(String email, Role role, String code) {
        Optional<Verification> searchedVerification = getVerification(code);

        if(searchedVerification.isEmpty()) {
            Verification verification = Verification.of(email,role,code);

            verificationRepository.save(verification);
            return ResponseEntity.ok(VERIFICATION_SUBJECT+ SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body( VERIFICATION_SUBJECT + " code already registered.");
    }
}
