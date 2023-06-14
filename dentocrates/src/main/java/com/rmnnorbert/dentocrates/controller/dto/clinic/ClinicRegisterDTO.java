package com.rmnnorbert.dentocrates.controller.dto.clinic;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClinicRegisterDTO(@NotBlank String name,
                                @NotBlank String clinicType,
                                @NotBlank String contactNumber,
                                String website,
                                @NotBlank LocationDTO location,
                                @NotBlank String street,
                                @NotBlank String openingHours,
                                @Min(1) long dentistId) {
}

