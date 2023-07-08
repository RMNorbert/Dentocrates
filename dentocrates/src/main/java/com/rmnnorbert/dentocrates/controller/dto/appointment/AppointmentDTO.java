package com.rmnnorbert.dentocrates.controller.dto.appointment;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDTO(@Min(1) long clinicId, @Min(1) long customerId, LocalDateTime reservation) {
    public static AppointmentDTO of(AppointmentCalendar calendar) {
        return AppointmentDTO.builder()
                .customerId(calendar.getCustomer().getId())
                .clinicId(calendar.getClinic().getId())
                .reservation(calendar.getReservation())
                .build();
    }
}
