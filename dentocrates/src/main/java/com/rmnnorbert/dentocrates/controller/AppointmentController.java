package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentUpdateDTO;
import com.rmnnorbert.dentocrates.service.AppointmentCalendarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class AppointmentController {
    private final AppointmentCalendarService appointmentCalendarService;
    @Autowired
    public AppointmentController(AppointmentCalendarService appointmentCalendarService) {
        this.appointmentCalendarService = appointmentCalendarService;
    }

    @GetMapping("/customer/{id}")
    public List<AppointmentDTO> getCustomerAppointments(@PathVariable Long id ){
        return appointmentCalendarService.getAllAppointmentById(id);
    }
    @PostMapping("/register")
    public ResponseEntity<String> addCalendar(@Valid @RequestBody AppointmentRegisterDTO appointmentDTO){
        return appointmentCalendarService.registerAppointment(appointmentDTO);
    }
    @DeleteMapping("/")
    public ResponseEntity<String> removeAppointment(@RequestBody DeleteDTO dto){
        return appointmentCalendarService.deleteAppointmentById(dto);
    }
    @GetMapping("/clinic/{id}")
    public List<AppointmentDTO> getAppointmentsByClinic(@PathVariable Long id ){
        return appointmentCalendarService.getAllAppointmentByClinic(id);
    }
    @PutMapping("/")
    public ResponseEntity<String> updateAppointment(@RequestBody AppointmentUpdateDTO dto) {
        return appointmentCalendarService.updateAppointment(dto);
    }
}
