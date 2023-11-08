package com.rmnnorbert.dentocrates.dto.review;

import jakarta.validation.constraints.NotNull;

public record ReviewRegisterDTO(@NotNull long reviewerId,
                                @NotNull long reviewedClinicId,
                                @NotNull long reviewedAppointmentId,
                                int rating,
                                String review) {
}
