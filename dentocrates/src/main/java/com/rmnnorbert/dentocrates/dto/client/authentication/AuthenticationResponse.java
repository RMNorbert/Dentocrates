package com.rmnnorbert.dentocrates.dto.client.authentication;

import lombok.Builder;


@Builder
public record AuthenticationResponse(String token, long id) {
}
