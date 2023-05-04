package com.rmnnorbert.dentocrates.controller.dto.client;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class CustomerResponseDTO {
    private final String email;
    private final String firstName;
    private final String lastname;
    private final int authorizationCategory;

    public CustomerResponseDTO of(Customer customer){
        return CustomerResponseDTO.builder()
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastname(customer.getLastName())
                .authorizationCategory(customer.getAuthorizationCategory())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerResponseDTO that = (CustomerResponseDTO) o;
        return authorizationCategory == that.authorizationCategory && email.equals(that.email) && firstName.equals(that.firstName) && lastname.equals(that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastname, authorizationCategory);
    }
}
