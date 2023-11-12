package com.rmnnorbert.dentocrates.dto.client.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(@NotBlank String verificationCode,
                               @NotBlank @Email String email,
                               @NotBlank @Size(min = 8) String password
) {
}
