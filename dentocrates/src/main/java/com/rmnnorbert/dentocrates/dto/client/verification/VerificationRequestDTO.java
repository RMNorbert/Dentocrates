package com.rmnnorbert.dentocrates.dto.client.verification;

import jakarta.validation.constraints.Email;

public record VerificationRequestDTO(@Email String email, String password) {
}
