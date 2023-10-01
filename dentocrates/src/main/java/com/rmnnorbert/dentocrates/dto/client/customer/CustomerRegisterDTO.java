package com.rmnnorbert.dentocrates.dto.client.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRegisterDTO(@Email String email,
                                  @NotBlank @Size(min = 8) String password,
                                  @NotBlank String firstName,
                                  @NotBlank String lastName) {
}
