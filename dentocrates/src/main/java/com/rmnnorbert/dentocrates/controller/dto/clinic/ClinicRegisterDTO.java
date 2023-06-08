package com.rmnnorbert.dentocrates.controller.dto.clinic;

public record ClinicRegisterDTO(String name,
                                String clinicType,
                                String contactNumber,
                                String website,
                                LocationDTO location,
                                String street,
                                String openingHours,
                                long dentistId) {
}
