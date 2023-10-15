package com.rmnnorbert.dentocrates.dto.review;

import jakarta.validation.constraints.NotNull;

public record ReviewRegisterDTO(@NotNull Long reviewerId,
                                @NotNull Long reviewedClinicId,
                                @NotNull Long reviewedAppointmentId,
                                int rating,
                                String review) {
}
