package org.rmnorbert.dentocrates.custom.exceptions;

public class InvalidOAuth2ClientRegistrationException extends RuntimeException{
    public InvalidOAuth2ClientRegistrationException() {
        super("Invalid or missing OAuth2 client registration for 'google'");
    }
}
