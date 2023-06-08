package com.rmnnorbert.dentocrates.controller.dto.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClinicResponseDTO {
    private final String name;
    private final String clinicType;
    private final String website;
    private final String contactNumber;
    private final int zipCode;
    private final String city;
    private final String street;
    private final String openingHours;
    private final long dentistId;

    public static ClinicResponseDTO of (Clinic clinic){
        return ClinicResponseDTO.builder()
                .name(clinic.getName())
                .clinicType(clinic.getClinicType().toString())
                .contactNumber(clinic.getContactNumber())
                .website(clinic.getWebsite())
                .zipCode(clinic.getLocation().getZipCode())
                .city(clinic.getLocation().getCity())
                .street(clinic.getStreet())
                .openingHours(clinic.getOpeningHours())
                .dentistId(clinic.getDentistInContract().getId())
                .build();
    }
}
