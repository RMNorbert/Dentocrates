package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.service.client.verification.VerificationEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationEmailService {
    private final GMailerService gMailerService;
    private final VerificationEntityService verificationEntityService;

    /** The subject line for verification emails. */
    public final static String VERIFICATION_SUBJECT = "Verification";
    @Autowired
    public VerificationEmailService(GMailerService gMailerService,
                                    VerificationEntityService verificationEntityService
                                    ) {
        this.gMailerService = gMailerService;
        this.verificationEntityService = verificationEntityService;
    }

    /**
     * Sends a verification email for a specified action to the provided email address.
     *
     * The method generates a unique verification code, registers it in the system, and sends an email
     * containing instructions for the specified action. If the action is a password reset, the message
     * includes the verification code for resetting the password.
     *
     * @param email The email address to which the verification email will be sent.
     * @param role The user role (e.g., "CUSTOMER", "DENTIST") for which the verification is requested.
     * @param action The action for which the user is being verified (e.g., "REGISTRATION", "PASSWORD_RESET").
     * @param reset Indicates whether the verification is for a password reset.
     * @return ResponseEntity indicating the status of the verification registration.
     */
    public ResponseEntity<String> sendVerification(String email, String role, String action, boolean reset) {
        String verificationCode = UUID.randomUUID().toString();
        String verificationMessage = "Verify " + action +
                " by login and to your profile page and send the verification code : ";
        Role roleAsEnum = Role.valueOf(role);
        ResponseEntity<String> response = verificationEntityService.registerVerification(email, roleAsEnum, verificationCode);

        if(reset) {
            verificationMessage += "reset password with: ";
        }
        verificationMessage += verificationCode;

        gMailerService.sendMail(email,VERIFICATION_SUBJECT,verificationMessage,GMailerService.BASE_URL);
        return response;
    }

    /**
     * Generates and sends an authentication code for user login.
     *
     * @param email The email address to which the authentication code will be sent.
     * @param role The user role (e.g., "CUSTOMER", "DENTIST") for which the authentication is requested.
     * @return The generated authentication code.
     */
    public String sendAuthenticationCode(String email, String role) {
        String authenticationCode = UUID.randomUUID().toString();
        String verificationMessage = "Authentication code to login : " + authenticationCode;
        String subject = "Dentocrates: Authentication code";
        Role roleAsEnum = Role.valueOf(role);

        verificationEntityService.registerVerification(email, roleAsEnum, authenticationCode);

        gMailerService.sendMail(email,subject,verificationMessage);
        return authenticationCode;
    }

}
