package com.rmnnorbert.dentocrates.controller;

/** Constants class for defining standardized API response-related values. */
public class ApiResponseConstants {
    // Media Type
    public static final String RESPONSE_MEDIA_TYPE = "application/json";

    // Response Content Messages
    public static final String INTERNAL_SERVER_ERROR_RESPONSE_CONTENT = "Internal server error";
    public static final String INVALID_CREDENTIALS_RESPONSE_CONTENT = "Invalid credentials provided";
    public static final String NOT_FOUND_RESPONSE_CONTENT = " not found.";
    public static final String SUCCESSFUL_REGISTER_RESPONSE_CONTENT = " successfully registered";
    public static final String SUCCESSFUL_UPDATE_RESPONSE_CONTENT = " successfully updated";
    public static final String DELETE_RESPONSE_CONTENT = " deleted successfully";
    public static final String INVALID_REQUEST_RESPONSE_CONTENT = "Invalid request to ";
    public static final String INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT = "Wrong username or password";
    public static final String INVALID_OAUTH2_CLIENT_REGISTRATION_EXCEPTION_RESPONSE_CONTENT =
            "Invalid or missing OAuth2 client registration for 'google'";
    public static final String DATA_INTEGRATION_RESPONSE_DESCRIPTION = "The provided data registered already";

    // HTTP Response Codes
    public static final String OK_RESPONSE_CODE = "200";
    public static final String CREATED_RESPONSE_CODE = "201";
    public static final String BAD_REQUEST_RESPONSE_CODE = "400";
    public static final String UNAUTHORIZED_RESPONSE_CODE = "401";
    public static final String NOT_FOUND_RESPONSE_CODE = "404";
    public static final String DATA_INTEGRATION_RESPONSE_CODE = "409";
    public static final String INTERNAL_SERVER_ERROR_RESPONSE_CODE = "500";

}
