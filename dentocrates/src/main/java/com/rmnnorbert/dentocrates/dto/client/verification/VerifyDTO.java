package com.rmnnorbert.dentocrates.dto.client.verification;

import jakarta.validation.constraints.NotBlank;

public record VerifyDTO(@NotBlank String verificationCode) {
}
