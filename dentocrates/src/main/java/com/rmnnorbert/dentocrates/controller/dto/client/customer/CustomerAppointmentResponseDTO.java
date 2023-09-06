package com.rmnnorbert.dentocrates.controller.dto.client.customer;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CustomerAppointmentResponseDTO (@Min(1) Long id,
                                              @NotBlank @NotNull String firstName,
                                              @NotBlank @NotNull String lastName
){
    public static CustomerAppointmentResponseDTO toDTO(Customer customer){
        return CustomerAppointmentResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
