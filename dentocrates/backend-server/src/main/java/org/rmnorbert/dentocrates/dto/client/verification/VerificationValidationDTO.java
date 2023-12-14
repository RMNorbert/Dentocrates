package org.rmnorbert.dentocrates.dto.client.verification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record VerificationValidationDTO(@NotNull String verificationCode, @Email String email) {
}
