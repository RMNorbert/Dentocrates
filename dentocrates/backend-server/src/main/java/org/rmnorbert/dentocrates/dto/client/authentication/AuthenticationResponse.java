package org.rmnorbert.dentocrates.dto.client.authentication;

import jakarta.validation.constraints.Min;
import lombok.Builder;


@Builder
public record AuthenticationResponse(String token,@Min(1) long id) {
}
