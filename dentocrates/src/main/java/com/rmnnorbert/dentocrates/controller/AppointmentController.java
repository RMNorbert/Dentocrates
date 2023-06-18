package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.service.AppointmentCalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentCalendarService appointmentCalendarService;
    @GetMapping("/customer/{id}")
    public List<AppointmentDTO> getCustomerAppointments(@PathVariable Long id ){
        return appointmentCalendarService.getAllAppointmentById(id);
    }
    @PostMapping("/register")
    public ResponseEntity<String> addCalendar(@Valid @RequestBody AppointmentDTO appointmentDTO){
        return appointmentCalendarService.registerAppointment(appointmentDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeAppointment(@PathVariable Long id ){
        return appointmentCalendarService.deleteAppointmentById(id);
    }
    @GetMapping("/clinic/{id}")
    public List<AppointmentDTO> getAppointmentsByClinic(@PathVariable Long id ){
        return appointmentCalendarService.getAllAppointmentByClinic(id);
    }
}
