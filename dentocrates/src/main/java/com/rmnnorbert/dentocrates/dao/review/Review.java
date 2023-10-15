package com.rmnnorbert.dentocrates.dao.review;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Review {
    @Id
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Customer reviewer;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Clinic reviewedClinic;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private AppointmentCalendar reviewedAppointment;

    private int rating;

    private String review;
}
