package com.rmnnorbert.dentocrates.security.auth;

import lombok.Builder;


@Builder
public record AuthenticationResponse(String token, long id) {
}
