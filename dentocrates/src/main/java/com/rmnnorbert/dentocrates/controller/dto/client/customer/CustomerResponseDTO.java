package com.rmnnorbert.dentocrates.controller.dto.client.customer;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.data.Role;
import lombok.Builder;

@Builder
public record CustomerResponseDTO(String email, String firstName, String lastname, Role role) {
    public static CustomerResponseDTO of(Customer customer) {
        return CustomerResponseDTO.builder()
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastname(customer.getLastName())
                .role(customer.getRole())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerResponseDTO that = (CustomerResponseDTO) o;
        return role == that.role && email.equals(that.email) && firstName.equals(that.firstName) && lastname.equals(that.lastname);
    }

}
