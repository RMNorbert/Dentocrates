package com.rmnnorbert.dentocrates.controller.dto.client.authentication;

import lombok.Builder;


@Builder
public record AuthenticationResponse(String token, long id) {
}
