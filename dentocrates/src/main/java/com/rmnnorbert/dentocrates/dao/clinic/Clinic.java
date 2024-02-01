package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.data.clinic.ClinicType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @With
    @Column(unique = true)
    @NotBlank
    private final String name;

    @Enumerated(EnumType.STRING)
    private final ClinicType clinicType;

    @With
    @NotBlank
    private final String contactNumber;

    @With
    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String website;

    @With
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private final Location location;

    @With
    @NotBlank
    private final String street;

    @With
    @NotBlank
    private final String openingHours;

    @ManyToOne
    @JoinColumn(name = "dentist_in_contract", referencedColumnName = "id")
    private final Client dentistInContract;

    public static Clinic of(ClinicRegisterDTO clinicRegisterDTO, Dentist dentist, Location clinicLocation) {
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

}

