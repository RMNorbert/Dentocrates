package com.rmnnorbert.dentocrates.service.client.communicationServices;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginNotificationService {
    private final GMailerService emailService;
    @Autowired
    public LoginNotificationService(GMailerService emailService) {
        this.emailService = emailService;
    }

    public boolean sendLoginNotification(String[] loginDetails) {
        String loginSubject = "Login Notification: Access from new computer";
        String email = loginDetails[0];
        String ipAddress = loginDetails[1];
        String message = getString(loginDetails, ipAddress);

        try {
            emailService.sendMail(email, loginSubject, message, GMailerService.BASE_URL);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @NotNull
    private static String getString(String[] loginDetails, String ipAddress) {
        String[] userAgent = loginDetails[2].split(" ");
        String os = userAgent[2];
        String browser = userAgent[8];
        String message = "Dear dentocrates user,\n\n It looks like you are trying to log in from a new device: " +
                "Ip address: " + ipAddress + " Operating system: " + os + " browser: " + browser + " ." +
                "If it wasn't you change your password and try to contact us as soon as possible." +
                "\n\nBest regards and keep safe,\nThe Dentocrates Team";
        return message;
    }
}
