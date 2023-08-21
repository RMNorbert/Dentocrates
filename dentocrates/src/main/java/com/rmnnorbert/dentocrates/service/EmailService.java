package com.rmnnorbert.dentocrates.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import static jakarta.mail.Message.RecipientType.TO;
@Service
public class EmailService {
    private final String senderUsername;
    private final String senderPassword;
    private final Properties prop;
    private Session session;
    public EmailService() {
        //configure credentials
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        this.senderUsername = System.getenv("SENDER_USERNAME");
        this.senderPassword = System.getenv("SENDER_PASSWORD");

        session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderUsername, senderPassword);
                    }
                });
    }

    public EmailService(String host, int port) {
        //configure credentials
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        this.senderUsername = System.getenv("SENDER_USERNAME");
        this.senderPassword = System.getenv("SENDER_PASSWORD");
    }
    public void sendMail(String recipient, String subject, String message, String link) {
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(System.getenv("EMAIL")));

            msg.setRecipients(TO, InternetAddress.parse(recipient));
            msg.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(message, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            if(link != null || !link.isEmpty() && link.startsWith("")) {
                MimeBodyPart mimeLinkBodyPart = new MimeBodyPart();
                mimeLinkBodyPart.setContent(link, "text/html; charset=utf-8");
                multipart.addBodyPart(mimeLinkBodyPart);
            }

            msg.setContent(multipart);

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(List<String> recipients, String subject, String message, String link) {
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(System.getenv("EMAIL")));
            for (String recipient : recipients) {

                msg.setRecipients(TO, InternetAddress.parse(recipient));
                msg.setSubject(subject);

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(message, "text/html; charset=utf-8");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                if (link != null || !link.isEmpty() && link.startsWith("")) {
                    MimeBodyPart mimeLinkBodyPart = new MimeBodyPart();
                    mimeLinkBodyPart.setContent(link, "text/html; charset=utf-8");
                    multipart.addBodyPart(mimeLinkBodyPart);
                }

                msg.setContent(multipart);

                Transport.send(msg);
            }
        } catch  (MessagingException e) {
            e.printStackTrace();
        }
    }
}
