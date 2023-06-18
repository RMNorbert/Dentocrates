package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.data.ClinicType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private final String name;

    @Enumerated(EnumType.STRING)
    private final ClinicType clinicType;
    @NotBlank
    private final String contactNumber;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String website;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private final Location location;
    @NotBlank
    private final String street;
    @NotBlank
    private final String openingHours;
    @OneToOne
    @JoinColumn(name = "dentist_in_contract", referencedColumnName = "id")
    private final Client dentistInContract;

    @OneToOne
    @PrimaryKeyJoinColumn
    private AppointmentCalendar appointmentCalendar;
    public static Clinic of(ClinicRegisterDTO clinicRegisterDTO,Dentist dentist,Location clinicLocation) {
        ClinicType type = ClinicType.valueOf(clinicRegisterDTO.clinicType());

        return Clinic.builder()
                .name(clinicRegisterDTO.name())
                .clinicType(type)
                .contactNumber(clinicRegisterDTO.contactNumber())
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

