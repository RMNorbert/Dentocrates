package org.rmnorbert.dentocrates.dao.review;


import jakarta.persistence.*;
import lombok.*;
import org.rmnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.dto.review.ReviewRegisterDTO;

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

    public static Review of(Customer customer, Clinic clinic, AppointmentCalendar reservation, ReviewRegisterDTO dto) {
        return Review.builder()
                .reviewer(customer)
                .reviewedClinic(clinic)
                .reviewedAppointment(reservation)
                .rating(dto.rating())
                .review(dto.review())
                .build();
    }
}
