package org.rmnorbert.dentocrates.custom.exceptions;

public class InvalidCredentialException extends RuntimeException{
    public InvalidCredentialException() {
        super("Wrong username or password");
    }
}
