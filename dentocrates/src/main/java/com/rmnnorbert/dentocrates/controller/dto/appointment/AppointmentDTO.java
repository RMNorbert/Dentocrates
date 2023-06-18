package com.rmnnorbert.dentocrates.controller.dto.appointment;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record AppointmentDTO(@Min(1) long clinicId, @Min(1) long CustomerId, LocalDate reservation) {
    public static AppointmentDTO of(AppointmentCalendar calendar) {
        return AppointmentDTO.builder()
                .CustomerId(calendar.getCustomer().getId())
                .clinicId(calendar.getClinic().getId())
                .reservation(calendar.getReservation())
                .build();
    }
}
