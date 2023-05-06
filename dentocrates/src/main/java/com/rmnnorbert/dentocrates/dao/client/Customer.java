package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.controller.dto.client.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany
    @JoinTable(
            name = "customer_appointment_calendar",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "appointment_calendar_id"))
    private List<AppointmentCalendar> appointmentCalendars = new ArrayList<>();


    @Transient
    private static final int CUSTOMER_AUTHORIZATION_CATEGORY = 3;
    @Builder
    public Customer(String email, String password, String firstName, String lastName, long id) {
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
