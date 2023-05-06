package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AppointmentCalendar {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "appointment_seq",
            sequenceName = "appointment_seq",
            allocationSize = 1
    )
    private Long id;
    @OneToOne
    @PrimaryKeyJoinColumn
    private final Clinic clinic;
    @ManyToMany(mappedBy = "appointmentCalendars")
    private List<Customer> customers = new ArrayList<>();
    private final LocalDate reservation;
}
