package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AppointmentCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Clinic clinic;
    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private Customer customer;
    private final LocalDate reservation;

}
