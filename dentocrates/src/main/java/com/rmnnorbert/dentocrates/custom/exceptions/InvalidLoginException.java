package com.rmnnorbert.dentocrates.custom.exceptions;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException() {
        super("Wrong username or password");
    }
}
