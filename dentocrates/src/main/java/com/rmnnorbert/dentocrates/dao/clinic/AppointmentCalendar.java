package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
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
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private final boolean appeared;
}
