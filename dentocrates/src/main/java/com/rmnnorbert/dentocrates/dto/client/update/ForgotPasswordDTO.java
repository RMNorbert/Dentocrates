package com.rmnnorbert.dentocrates.dto.client.update;

import jakarta.validation.constraints.Email;

public record ForgotPasswordDTO(@Email String requesterEmail) {
}
