package com.rmnnorbert.dentocrates.dto.appointment;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record AppointmentRegisterDTO(@Min(1) long clinicId,
                                     @Min(1) long customerId,
                                     LocalDateTime reservation
                                    ) {
}

