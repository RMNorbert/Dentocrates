package com.rmnnorbert.dentocrates.security.auth;

import lombok.Getter;
import lombok.Setter;
/**
 * Represents the response received from the OAuth 2.0 server when exchanging
 * authorization code for tokens. The fields in this class correspond to the
 * properties returned in the OAuth 2.0 response.
 */
@Getter
@Setter
public class TokenResponse {
    // Fields representing properties in the OAuth 2.0 response
    private String access_token;
    private int expires_in;
    private String id_token;
    private String scope;
    private String token_type;
    private String refresh_token;
}

