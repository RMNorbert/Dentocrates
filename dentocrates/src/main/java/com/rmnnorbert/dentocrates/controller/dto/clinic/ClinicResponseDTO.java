package com.rmnnorbert.dentocrates.controller.dto.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ClinicResponseDTO(@Min(1) long id,
                                @NotBlank String name,
                                @NotBlank String clinicType,
                                @NotBlank String website,
                                @NotBlank String contactNumber,
                                @Min(1000) @Max(10000) int zipCode,
                                @NotBlank String city,
                                @NotBlank String street,
                                @NotBlank String openingHours,
                                @Min(1) long dentistId) {
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
                .dentistId(clinic.getDentistInContract().getId())
                .build();
    }
}
