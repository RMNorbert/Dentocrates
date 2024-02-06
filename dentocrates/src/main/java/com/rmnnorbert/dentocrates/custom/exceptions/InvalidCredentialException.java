package com.rmnnorbert.dentocrates.custom.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException() {
        super("Wrong username or password");
    }
}
