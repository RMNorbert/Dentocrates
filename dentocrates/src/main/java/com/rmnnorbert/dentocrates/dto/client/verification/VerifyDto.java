package com.rmnnorbert.dentocrates.dto.client.verification;

import jakarta.validation.constraints.NotBlank;

public record VerifyDto(@NotBlank String verificationCode) {
}
