package com.rmnnorbert.dentocrates.controller.dto.client.dentist;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DentistRegisterDTO(@Email String email,
                                 @NotBlank @Size(min = 8) String password,
                                 @NotBlank String firstName,
                                 @NotBlank String lastName,
                                 @NotBlank String operatingLicenceNo) {
}
