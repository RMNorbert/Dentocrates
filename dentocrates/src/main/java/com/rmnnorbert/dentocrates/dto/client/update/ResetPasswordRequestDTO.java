package com.rmnnorbert.dentocrates.dto.client.update;

import jakarta.validation.constraints.Email;

public record ResetPasswordRequestDTO(@Email String email, String role) {
}
