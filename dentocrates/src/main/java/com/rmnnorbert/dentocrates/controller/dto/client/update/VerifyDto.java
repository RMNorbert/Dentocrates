package com.rmnnorbert.dentocrates.controller.dto.client.update;

import jakarta.validation.constraints.NotBlank;

public record VerifyDto(@NotBlank String verificationCode) {
}
