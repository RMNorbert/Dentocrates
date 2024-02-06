package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.rmnnorbert.dentocrates.custom.exceptions.EmailSendingException;
import com.rmnnorbert.dentocrates.dto.client.contact.ContactMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    /** The email address of the Dentocrates representative where the contact messages will be sent. */
    private final static String SUPPORT_TEAM_EMAIL_ADDRESS= System.getenv("SENDER_USERNAME");
    private final GMailerService emailService;
    @Autowired
    public ContactService(GMailerService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends a contact message to the Dentocrates support team.
     *
     * This method constructs the message content using the sender's email, message body,
     * and subject from the provided ContactMessageDTO. It then sends the message to the
     * Dentocrates support team's email address using the emailService.
     *
     * @param message The ContactMessageDTO containing sender's details, message, and subject.
     * @return True if the message was sent successfully; throw EmailException otherwise.
     */
    public boolean sendMessage(ContactMessageDTO message) {
        try {
            String messageToSend = String.format("Contact email: %s :\n %s", message.sender(), message.message());
            emailService.sendMail(SUPPORT_TEAM_EMAIL_ADDRESS, message.subject(), messageToSend, "");
            return true;
        } catch (Exception e) {
            throw new EmailSendingException("contact message");
        }
    }
}
