package org.rmnorbert.dentocrates.dto.client.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.data.authentication.Role;

@Builder
public record CustomerResponseDTO(@Min(1) long id,
                                  @NotBlank String email,
                                  @NotBlank String firstName,
                                  @NotBlank String lastname,
                                  Role role,
                                  Boolean verified
) {
    public static CustomerResponseDTO of(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastname(customer.getLastName())
                .role(customer.getRole())
                .verified(customer.isVerified())
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
