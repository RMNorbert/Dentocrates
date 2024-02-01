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
    /** The base URL for Dentocrates services obtained from the environment variables. */
    public final static String BASE_URL = System.getenv("BASE_URL");
    /** The name of the Dentocrates application. */
    private final static String APPLICATION_NAME = "Dentocrates";
    /** The user ID representing the sender in Gmail, typically set to "me". */
    private final static String SENDER_USER_ID = "me";
    /** The GsonFactory used for JSON processing. */
    private final static GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** The email address of the Dentocrates representative sender obtained from the environment variables. */
    private final static String EMAIL_ADDRESS = System.getenv("SENDER_USERNAME");
    /** The path to the directory where Gmail tokens are stored.*/
    private final static String PATH_TO_TOKEN_DIRECTORY = "tokens";
    /** The access type used in OAuth2 flow. */
    private final static String FLOW_ACCESS_TYPE = "offline";
    /** The port number on which the receiver server is running, set to 8080. */
    private final static int RECEIVER_SERVER_PORT = 8080;
    /** The Gmail service used for communication with Gmail API. */
    private final Gmail service;
    /** The NetHttpTransport used for communication with Gmail API. */
    private final NetHttpTransport httpTransport;

    /**
     * Constructor for initializing the GMailerService.
     * It sets up the necessary HTTP transport, builds a Gmail service,
     * and obtains the credentials required for communication with Gmail API.
     *
     * @throws GeneralSecurityException If a security-related error occurs during setup.
     * @throws IOException              If an I/O error occurs during setup.
     */
    public GMailerService() throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport, JSON_FACTORY))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Sends an email using the Gmail API.
     *
     * This method prepares and sends an email to the specified recipient with the given subject,
     * message, and an optional link. It uses the Gmail API service to send the email.
     *
     * @param recipient The email address of the recipient.
     * @param subject   The subject of the email.
     * @param message   The content of the email.
     * @param link      An optional link to be included in the email.
     */
    public void sendMail(String recipient, String subject, String message, String link) {
        try {
            Session session = initializeSession();

            byte[] rawMessageBytes = createEmailContentBytes(session, recipient, subject, message, link);
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            Message msg = new Message();
            msg.setRaw(encodedEmail);

            service.users().messages().send(SENDER_USER_ID, msg).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMail(String recipient, String subject, String message) {
        try {
            Session session = initializeSession();

            byte[] rawMessageBytes = createEmailContentBytes(session, recipient, subject, message);
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            Message msg = new Message();
            msg.setRaw(encodedEmail);

            service.users().messages().send(SENDER_USER_ID, msg).execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the byte array representation of an email content.
     *
     * This method constructs a MimeMessage using the provided JavaMail Session,
     * sets the sender, recipient, subject, and message content with an optional link.
     * It then converts the MimeMessage to a byte array for further processing.
     *
     * @param session   The JavaMail Session used for email communication.
     * @param recipient The email address of the recipient.
     * @param subject   The subject of the email.
     * @param message   The main content of the email.
     * @param link      An optional link to be included in the email.
     * @return The byte array representation of the email content.
     * @throws MessagingException If an error occurs during the creation of the MimeMessage.
     * @throws IOException       If an I/O error occurs during the conversion to byte array.
     */
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

    private byte[] createEmailContentBytes(Session session, String recipient, String subject, String message)
            throws MessagingException, IOException {

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(EMAIL_ADDRESS));
        email.addRecipient(TO, new InternetAddress(recipient));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        return buffer.toByteArray();
    }

    /**
     * Obtains user credentials for Gmail API using the OAuth 2.0 authorization code flow.
     *
     * This method loads client secrets from a JSON file, initializes the authorization code flow,
     * and launches a local server for user authorization. It returns the obtained user credentials.
     *
     * @param HTTP_TRANSPORT The network transport for communication.
     * @param jsonFactory    The GsonFactory for JSON processing.
     * @return The user credentials for Gmail API.
     */
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

    /**
     * Builds and configures the Google Authorization Code Flow for Gmail API.
     *
     * This method constructs a GoogleAuthorizationCodeFlow using the provided parameters:
     * - HTTP_TRANSPORT: The network transport used for communication.
     * - jsonFactory: The GsonFactory used for JSON processing.
     * - clientSecrets: The client secrets obtained during application registration.
     *
     * The flow is configured with necessary settings such as required scopes, data store,
     * and access type for offline access.
     *
     * @param HTTP_TRANSPORT The network transport for communication.
     * @param jsonFactory    The GsonFactory for JSON processing.
     * @param clientSecrets  The client secrets obtained during application registration.
     * @return The configured GoogleAuthorizationCodeFlow for Gmail API.
     * @throws IOException If an I/O error occurs during the construction of the flow.
     */
    private static GoogleAuthorizationCodeFlow buildFlow(NetHttpTransport HTTP_TRANSPORT,
                                                         GsonFactory jsonFactory,
                                                         GoogleClientSecrets clientSecrets) throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(PATH_TO_TOKEN_DIRECTORY).toFile()))
                .setAccessType(FLOW_ACCESS_TYPE)
                .build();
    }

    /**
     * Obtains a JavaMail Session for email communication.
     *
     * This method initializes a Properties object and retrieves a default JavaMail Session instance.
     * The Session object can be used to configure and establish email communication.
     * @return The JavaMail Session instance.
     */
    private Session initializeSession() {
        Properties props = new Properties();
        return Session.getDefaultInstance(props, null);
    }
}

