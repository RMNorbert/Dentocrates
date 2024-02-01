package com.rmnnorbert.dentocrates.custom.exceptions;


import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    /**
     * Exception handler method for handling MethodArgumentNotValidException.
     * This method is invoked when there are validation errors in method arguments.
     *
     * @param exception The MethodArgumentNotValidException thrown.
     * @return ResponseEntity containing a bad request status and error message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    /**
     * Exception handler method for handling DataIntegrityViolationException.
     * This method is invoked when there is a violation of data integrity, such as unique constraint violation.
     *
     * @param exception The DataIntegrityViolationException thrown.
     * @return ResponseEntity containing a conflict status and extracted error message.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String cause = "";
        if (exception.getRootCause() != null) {
            cause = exception.getRootCause().getLocalizedMessage();
        }
        int detailStartIndex = cause.indexOf("=");
        String message = cause.substring(detailStartIndex + 1).trim().replaceAll("[()]", "");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    /**
     * Exception handler method for handling NotFoundException.
     * This method is invoked when a resource is not found, resulting in a 404 Not Found status.
     *
     * @param exception The NotFoundException thrown.
     * @return ResponseEntity containing a not found status and the error message.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Exception handler method for handling database-related exceptions, such as SQLException and PersistenceException.
     * This method is invoked when an error occurs while accessing the database, resulting
     * in a 500 Internal Server Error status.
     *
     * @return ResponseEntity containing an internal server error status and a generic error message.
     */
    @ExceptionHandler({SQLException.class, PersistenceException.class})
    public ResponseEntity<Object> handleDatabaseException() {
        String errorMessage = "An error occurred while accessing the database.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    /**
     * Exception handler method for handling InvalidCredentialException.
     * This method is invoked when an unauthorized access attempt is detected, resulting in a 401 Unauthorized status.
     *
     * @param exception The InvalidCredentialException that was thrown.
     * @return ResponseEntity containing an unauthorized status and the exception message.
     */
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    /**
     * Exception handler method for handling InvalidOAuth2ClientRegistrationException.
     * This method is invoked when an invalid OAuth2 client registration is detected,
     * resulting in a 500 Internal Server Error status.
     *
     * @param exception The InvalidOAuth2ClientRegistrationException that was thrown.
     * @return ResponseEntity containing an internal server error status and the exception message.
     */
    @ExceptionHandler(InvalidOAuth2ClientRegistrationException.class)
    public ResponseEntity<String> handleInvalidOAuth2ClientRegistrationException(
            InvalidOAuth2ClientRegistrationException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
