package com.rmnnorbert.dentocrates.custom.exceptions;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super("Error during sending email :" + message);
    }
}
