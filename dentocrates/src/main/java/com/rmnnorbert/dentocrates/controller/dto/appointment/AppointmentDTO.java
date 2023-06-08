package com.rmnnorbert.dentocrates.controller.dto.appointment;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AppointmentDTO(ClinicResponseDTO clinicResponseDTO,
                             LocalDate reservation) {
    public static AppointmentDTO of(AppointmentCalendar appointmentCalendar){
        return AppointmentDTO.builder()
                .clinicResponseDTO(ClinicResponseDTO.of(appointmentCalendar.getClinic()))
                .reservation(appointmentCalendar.getReservation()).build();
    }
}
