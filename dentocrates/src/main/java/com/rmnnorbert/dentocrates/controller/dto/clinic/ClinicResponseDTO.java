package com.rmnnorbert.dentocrates.controller.dto.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import lombok.Builder;

@Builder
public record ClinicResponseDTO(long id, String name, String clinicType, String website, String contactNumber, int zipCode,
                                String city, String street, String openingHours, long dentistId) {
    public static ClinicResponseDTO of(Clinic clinic) {
        return ClinicResponseDTO.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .clinicType(clinic.getClinicType().toString())
                .contactNumber(clinic.getContactNumber())
                .website(clinic.getWebsite())
                .zipCode(clinic.getLocation().getZipCode())
                .city(clinic.getLocation().getCity())
                .street(clinic.getStreet())
                .openingHours(clinic.getOpeningHours())
                .dentistId(clinic.getDentistInContract().id)
                .build();
    }
}
