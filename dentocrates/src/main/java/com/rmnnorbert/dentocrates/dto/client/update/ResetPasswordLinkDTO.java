package com.rmnnorbert.dentocrates.dto.client.update;

import jakarta.validation.constraints.Email;

public record ResetPasswordLinkDTO(@Email String email, String role) {
}
