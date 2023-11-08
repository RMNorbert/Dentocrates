package com.rmnnorbert.dentocrates.dto.clinic;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClinicRegisterDTO(@NotBlank String name,
                                @NotBlank String clinicType,
                                @NotBlank String contactNumber,
                                String website,
                                @Min(1000)
                                @Max(10000) int zipCode,
                                @NotBlank String city,
                                @NotBlank String street,
                                @NotBlank String openingHours,
                                @Min(1) long dentistId) {
}

