package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "clinic_seq",
            sequenceName = "clinic_seq",
            allocationSize = 1
    )
    private Long id;
    private final String name;
    @Enumerated(EnumType.STRING)
    private final ClinicType clinicType;
    private final String contactNumber;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String website;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private final Location location;
    private final String street;
    private final String openingHours;
    @OneToOne
    @JoinColumn(name = "dentist_in_contract", referencedColumnName = "id")
    private final Dentist dentistInContract;

    @OneToOne
    @PrimaryKeyJoinColumn
    private AppointmentCalendar appointmentCalendar;
    public static Clinic of(ClinicRegisterDTO clinicRegisterDTO,Dentist dentist,Location clinicLocation) {
        ClinicType type = ClinicType.valueOf(clinicRegisterDTO.clinicType());

        return Clinic.builder()
                .name(clinicRegisterDTO.name())
                .clinicType(type)
                .contactNumber(builder().contactNumber)
                .website(clinicRegisterDTO.website())
                .location(clinicLocation)
                .street(clinicRegisterDTO.street())
                .openingHours(clinicRegisterDTO.openingHours())
                .dentistInContract(dentist)
                .build();
    }
    public void setWebsite(String newWebsite){
        website = newWebsite;
    }
    public void setAppointmentCalendar(AppointmentCalendar registeredAppointmentCalendar){
        appointmentCalendar = registeredAppointmentCalendar;
    }
}
