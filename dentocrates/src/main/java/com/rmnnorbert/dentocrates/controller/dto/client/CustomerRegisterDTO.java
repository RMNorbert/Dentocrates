package com.rmnnorbert.dentocrates.controller.dto.client;

public record CustomerRegisterDTO(String email,
                                  String password,
                                  String firstName,
                                  String lastname) {
}
