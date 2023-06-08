package com.rmnnorbert.dentocrates.controller.dto.appointment;

import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record AppointmentRegisterDTO(@Min(1) long clinicId,@Min(1) long CustomerId, LocalDate reservation) {
}
