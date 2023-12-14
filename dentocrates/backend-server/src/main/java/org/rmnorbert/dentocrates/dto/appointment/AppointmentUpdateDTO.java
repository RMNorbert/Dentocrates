package org.rmnorbert.dentocrates.dto.appointment;

import jakarta.validation.constraints.Min;

public record AppointmentUpdateDTO(@Min(1) long id,
                                   @Min(1) long clinicId,
                                   @Min(1) long dentistId,
                                   boolean appeared) {
}
