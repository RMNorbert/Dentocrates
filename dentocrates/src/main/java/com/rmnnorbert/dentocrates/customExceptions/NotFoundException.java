package com.rmnnorbert.dentocrates.customExceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message + " not found.");
    }
}
