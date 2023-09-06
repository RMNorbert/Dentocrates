package com.rmnnorbert.dentocrates.service.client;

import com.rmnnorbert.dentocrates.dao.verification.Verification;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class VerificationService {
    private final GMailerService gMailerService;
    private final VerificationRepository verificationRepository;
    private final static String VERIFICATION_SUBJECT = "Verification";
    @Autowired
    public VerificationService(GMailerService gMailerService, VerificationRepository verificationRepository) {
        this.gMailerService = gMailerService;
        this.verificationRepository = verificationRepository;
    }
    public Optional<Verification> getVerification(String verificationCode) {
        return verificationRepository.findByVerificationCode(verificationCode);
    }
    public boolean validate(String verificationCode) {
        return getVerification(verificationCode).isPresent();
    }
    public ResponseEntity<String> sendVerification(String email, String role, String action, boolean reset) {
        String verificationCode = UUID.randomUUID().toString();
        String verificationMessage = "Verify " + action + " by clicking on the following link.";
        String verificationUrl = "http://localhost:3000/verify/";
        Role roleAsEnum = Role.valueOf(role);

        ResponseEntity<String> response = registerVerification(email, roleAsEnum, verificationCode);
        String link;
        if(!reset) {
            link = verificationUrl + verificationCode;
        } else {
            verificationUrl += "reset/";
            link = verificationUrl + verificationCode;
        }
        gMailerService.sendMail(email,VERIFICATION_SUBJECT,verificationMessage,link);
        return response;
    }
    public String sendAuthenticationCode(String email, String role) {
        String authenticationCode = UUID.randomUUID().toString();
        String verificationMessage = "Authentication code to login : " + authenticationCode;
        String subject = "Dentocrates: Authentication code";
        Role roleAsEnum = Role.valueOf(role);

        registerVerification(email, roleAsEnum, authenticationCode);

        gMailerService.sendMail(email,subject,verificationMessage,"");
        return authenticationCode;
    }
    public ResponseEntity<String> deleteVerification(String verificationCode) {
        try {
            Optional<Verification> verification = getVerification(verificationCode);
            if (verification.isPresent()) {
                verificationRepository.delete(verification.get());
                return ResponseEntity.ok("Verification request successfully deleted.");
            } else {
                return ResponseEntity.badRequest().body("There is no verification request for the provided email.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }
    }

    private ResponseEntity<String> registerVerification(String email, Role role, String code) {
        Optional<Verification> searchedVerification = getVerification(code);

        if(searchedVerification.isEmpty()) {
            Verification verification = Verification.builder()
                    .verificationCode(code)
                    .email(email)
                    .role(role)
                    .build();

            verificationRepository.save(verification);
            return ResponseEntity.ok("Verification request successfully registered.");
        } else {
            return ResponseEntity.badRequest().body("A verification request already registered.");
        }
    }

}
