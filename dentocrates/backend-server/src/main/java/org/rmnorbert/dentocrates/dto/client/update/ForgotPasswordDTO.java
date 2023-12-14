package org.rmnorbert.dentocrates.dto.client.update;

import jakarta.validation.constraints.Email;

public record ForgotPasswordDTO(@Email String requesterEmail) {
}
