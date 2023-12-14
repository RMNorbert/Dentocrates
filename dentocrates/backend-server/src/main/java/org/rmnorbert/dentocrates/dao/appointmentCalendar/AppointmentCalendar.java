package org.rmnorbert.dentocrates.dao.appointmentCalendar;


import jakarta.persistence.*;
import lombok.*;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class AppointmentCalendar {

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
