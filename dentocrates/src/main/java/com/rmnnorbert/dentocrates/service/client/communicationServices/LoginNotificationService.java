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

    public boolean sendLoginNotification(String ipAddress, String userAgent, String email) {
        String loginSubject = "Login Notification: Access from new computer";
        String message = getString(userAgent, ipAddress);

        try {
            emailService.sendMail(email, loginSubject, message, GMailerService.BASE_URL);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @NotNull
    private static String getString(String userAgent, String ipAddress) {
        String[] userAgentArray = userAgent.split(" ");
        String os = userAgentArray[2];
        String browser = userAgentArray[7];
        String message = "Dear dentocrates user,\n\n It looks like you are trying to log in from a new device: " +
                "Ip address: " + ipAddress + " Operating system: " + os + " browser: " + browser + " ." +
                "If it wasn't you change your password and try to contact us as soon as possible." +
                "\n\nBest regards and keep safe,\nThe Dentocrates Team";
        return message;
    }
}
