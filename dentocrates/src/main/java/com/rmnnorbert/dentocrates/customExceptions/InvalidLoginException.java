package com.rmnnorbert.dentocrates.customExceptions;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException() {
        super("Wrong username or password");
    }
}
