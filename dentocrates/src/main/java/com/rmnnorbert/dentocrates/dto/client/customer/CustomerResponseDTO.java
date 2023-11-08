package com.rmnnorbert.dentocrates.dto.client.customer;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CustomerResponseDTO(@Min(1) long id,
                                  @NotBlank String email,
                                  @NotBlank String firstName,
                                  @NotBlank String lastname,
                                  Role role) {
    public static CustomerResponseDTO of(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
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
