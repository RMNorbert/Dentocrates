package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.controller.dto.client.CustomerRegisterDTO;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@NonNull
@NoArgsConstructor(force = true)
public class Customer extends Client {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_seq"
    )
    @SequenceGenerator(
            name = "customer_seq",
            sequenceName = "customer_seq",
            allocationSize = 1
    )
    private final Long id;
    @Transient
    private static final int CUSTOMER_AUTHORIZATION_CATEGORY = 3;
    @Builder
    public Customer(String email, String password, String firstName, String lastName, Long id) {
        super(email, password, firstName, lastName, CUSTOMER_AUTHORIZATION_CATEGORY);
        this.id = id;
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
