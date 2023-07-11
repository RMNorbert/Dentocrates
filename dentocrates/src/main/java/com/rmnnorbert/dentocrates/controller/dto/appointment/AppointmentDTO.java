package com.rmnnorbert.dentocrates.controller.dto.appointment;

import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDTO(@Min(1) long id,
                             @Min(1) long clinicId,
                             @Min(1) long customerId,
                             LocalDateTime reservation,
                             boolean appeared
                            ) {
    public static AppointmentDTO of(AppointmentCalendar calendar) {
        return AppointmentDTO.builder()
                .id(calendar.getId())
                .customerId(calendar.getCustomer().getId())
                .clinicId(calendar.getClinic().getId())
                .reservation(calendar.getReservation())
                .appeared(calendar.isAppeared())
                .build();
    }
}
