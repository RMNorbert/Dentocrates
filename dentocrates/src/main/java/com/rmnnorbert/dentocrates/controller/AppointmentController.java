package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentRegisterDTO;
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
    @GetMapping
    public List<AppointmentDTO> getAppointments(@RequestBody long id ){
        return appointmentCalendarService.getAllAppointmentById(id);
    }
    @GetMapping("/all")
    public List<AppointmentDTO> getAllAppointments(){
        return appointmentCalendarService.getAllAppointment();
    }
    @PostMapping("/register")
    public ResponseEntity<String> addCalendar(@Valid @RequestBody AppointmentRegisterDTO appointmentRegisterDTO){
        return appointmentCalendarService.registerAppointment(appointmentRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> addCalendar(@PathVariable long id ){
        return appointmentCalendarService.deleteAppointmentById(id);
    }

}
