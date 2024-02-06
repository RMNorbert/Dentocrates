package com.rmnnorbert.dentocrates.service.client.communicationServices;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginNotificationService {
    private final GMailerService emailService;
    /** The index in the userAgentArray split array representing the operating system information in the user agent. */
    private final static int USER_AGENT_OS_INDEX = 2;
    /** The index in the userAgentArray split array representing the browser information in the user agent. */
    private final static int USER_AGENT_BROWSER_INDEX = 7;
    @Autowired
    public LoginNotificationService(GMailerService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends a login notification email to the user about access from a new computer.
     *
     * @param ipAddress The IP address from which the login attempt is made.
     * @param userAgent The user agent string from the login request.
     * @param email     The email address of the user.
     * @return True if the login notification email is successfully sent, false otherwise.
     */
    public boolean sendLoginNotification(String ipAddress, String userAgent, String email) {
        String loginSubject = "Login Notification: Access from new computer";
        String message = generateSecurityAlertMessage(userAgent, ipAddress);
        try {
            emailService.sendMail(email, loginSubject, message, GMailerService.BASE_URL);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Generates a security alert message for a new device login attempt.
     *
     * @param userAgent The user agent string from the login request.
     * @param ipAddress The IP address from which the login attempt is made.
     * @return A security alert message containing details about the login attempt.
     */
    @NotNull
    private static String generateSecurityAlertMessage(String userAgent, String ipAddress) {
        String[] userAgentArray = userAgent.split(" ");
        String os = userAgentArray[USER_AGENT_OS_INDEX];
        String browser = userAgentArray[USER_AGENT_BROWSER_INDEX];
        String message = "Dear dentocrates user,\n\n It looks like you are trying to log in from a new device: " +
                "Ip address: " + ipAddress + " Operating system: " + os + " browser: " + browser + " ." +
                "If it wasn't you change your password and try to contact us as soon as possible." +
                "\n\nBest regards and keep safe,\nThe Dentocrates Team";
        return message;
    }
}
