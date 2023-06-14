package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.data.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Customer extends Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private static final Role CUSTOMER_ROLE = Role.CUSTOMER;
    @Builder
    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, CUSTOMER_ROLE);
    }


    public static Customer of(CustomerRegisterDTO customerRegisterDTO) {
        return Customer.builder()
                .email(customerRegisterDTO.email())
                .password(customerRegisterDTO.password())
                .firstName(customerRegisterDTO.firstName())
                .lastName(customerRegisterDTO.lastname())
                .build();
    }
}
