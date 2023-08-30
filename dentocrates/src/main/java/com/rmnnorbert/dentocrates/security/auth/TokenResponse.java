package com.rmnnorbert.dentocrates.security.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String access_token;
    private int expires_in;
    private String id_token;
    private String scope;
    private String token_type;
    private String refresh_token;
}

