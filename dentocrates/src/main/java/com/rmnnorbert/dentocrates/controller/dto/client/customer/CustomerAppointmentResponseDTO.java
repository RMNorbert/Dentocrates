package com.rmnnorbert.dentocrates.controller.dto.client.customer;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import lombok.Builder;

@Builder
public record CustomerAppointmentResponseDTO (Long id, String firstName, String lastName){
    public static CustomerAppointmentResponseDTO toDTO(Customer customer){
        return CustomerAppointmentResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
