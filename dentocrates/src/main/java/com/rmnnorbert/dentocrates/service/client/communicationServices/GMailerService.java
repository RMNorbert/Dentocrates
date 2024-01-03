package com.rmnnorbert.dentocrates.service.client.communicationServices;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Set;
import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

@Service
public class GMailerService {
    public final static String BASE_URL = System.getenv("BASE_URL");
    private final static String APPLICATION_NAME = "Dentocrates";
    private final static String SENDER_USER_ID = "me";
    private final static GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final static String EMAIL_ADDRESS = System.getenv("SENDER_USERNAME");
    private final static String PATH_TO_TOKEN_DIRECTORY = "tokens";
    private final static String FLOW_ACCESS_TYPE = "offline";
    private final static int RECEIVER_SERVER_PORT = 8080;
    private final Gmail service;
    private final NetHttpTransport httpTransport;
    public GMailerService() throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport, JSON_FACTORY))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void sendMail(String recipient, String subject, String message, String link) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            byte[] rawMessageBytes = createEmailContentBytes(session, recipient, subject, message, link);
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message msg = new Message();
            msg.setRaw(encodedEmail);

            service.users().messages().send(SENDER_USER_ID, msg).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private byte[] createEmailContentBytes(Session session, String recipient, String subject, String message, String link)
            throws MessagingException, IOException {

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(EMAIL_ADDRESS));
        email.addRecipient(TO, new InternetAddress(recipient));
        email.setSubject(subject);
        email.setText(message + "\n" + link);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        return buffer.toByteArray();
    }
    private static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory) {
        try {
            // Load client secrets. Replace the resourcePath with your client_secret json file
            String resourcePath = "/client_secret_49338607330-aona1jlm9qs2m7r7rhni97e86hi9d0b7.apps.googleusercontent.com.json";

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                    new InputStreamReader(GMailerService.class.getResourceAsStream(resourcePath)));

            GoogleAuthorizationCodeFlow flow = buildFlow(HTTP_TRANSPORT, jsonFactory, clientSecrets);

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(RECEIVER_SERVER_PORT).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize(APPLICATION_NAME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Credential(new Credential.AccessMethod() {
                @Override
                public void intercept(HttpRequest httpRequest, String s){}
                @Override
                public String getAccessTokenFromRequest(HttpRequest httpRequest) {
                    return null;
                }
            });
        }
    }
    private static GoogleAuthorizationCodeFlow buildFlow(NetHttpTransport HTTP_TRANSPORT,
                                                         GsonFactory jsonFactory,
                                                         GoogleClientSecrets clientSecrets) throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(PATH_TO_TOKEN_DIRECTORY).toFile()))
                .setAccessType(FLOW_ACCESS_TYPE)
                .build();
    }
}

