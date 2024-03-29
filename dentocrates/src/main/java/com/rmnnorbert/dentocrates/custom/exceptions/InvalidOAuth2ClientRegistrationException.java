package com.rmnnorbert.dentocrates.custom.exceptions;

public class InvalidOAuth2ClientRegistrationException extends RuntimeException {
    public InvalidOAuth2ClientRegistrationException() {
        super("Invalid or missing OAuth2 client registration for 'google'.");
    }
    public InvalidOAuth2ClientRegistrationException(String message) {
        super("Invalid or missing OAuth2 client registration for 'google'." + " Details: " + message);
    }
}
