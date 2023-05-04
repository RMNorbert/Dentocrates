package com.rmnnorbert.dentocrates.controller.dto.client;

public record DentistRegisterDTO(String email,
                                 String password,
                                 String firstName,
                                 String lastname,
                                 String operatingLicenceNo) {
}
