package com.rmnnorbert.dentocrates.dao.review;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewer", referencedColumnName = "id")
    @NonNull
    private final Customer reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_clinic", referencedColumnName = "id")
    @NonNull
    private final Clinic reviewedClinic;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "reviewed_appointment", referencedColumnName = "id")
    private AppointmentCalendar reviewedAppointment;

    private int rating;

    private String review;
}
