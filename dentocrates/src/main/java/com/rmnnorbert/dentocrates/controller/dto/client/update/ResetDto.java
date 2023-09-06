package com.rmnnorbert.dentocrates.controller.dto.client.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetDto(@NotBlank String verificationCode,
                       @NotBlank @Email String email,
                       @NotBlank @Size(min = 8) String password
) {
}
