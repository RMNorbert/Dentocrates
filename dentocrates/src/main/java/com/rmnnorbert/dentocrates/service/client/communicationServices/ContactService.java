package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.rmnnorbert.dentocrates.dto.client.contact.ContactMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final static String SUPPORT_TEAM_EMAIL_ADDRESS= System.getenv("SENDER_USERNAME");
    private final GMailerService emailService;
    @Autowired
    public ContactService(GMailerService emailService) {
        this.emailService = emailService;
    }
    public boolean sendMessage(ContactMessageDTO message) {
        try {
            String messageToSend = String.format("Contact email: %s :\n %s", message.sender(), message.message());
            emailService.sendMail(SUPPORT_TEAM_EMAIL_ADDRESS, message.subject(), messageToSend, "");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
