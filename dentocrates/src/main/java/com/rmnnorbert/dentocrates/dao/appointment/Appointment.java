package com.rmnnorbert.dentocrates.dao.appointment;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Clinic clinic;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Customer customer;

    private final LocalDateTime reservation;

    @With
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private final boolean appeared;

    @With
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private final boolean reviewed;
}
