package com.rmnnorbert.dentocrates.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AuthenticationRequest {
    @Email
    private final String email;
    @NotBlank
    @Size(min = 8)
    private final String password;
}
