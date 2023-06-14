package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentCalendarService {
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    private final CustomerRepository customerRepository;
    private final ClinicRepository clinicRepository;
    public List<AppointmentDTO> getAllAppointment(){
        return appointmentCalendarRepository.findAll()
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }
    public List<AppointmentDTO> getAllAppointmentById(long id){
        return appointmentCalendarRepository.getAllByCustomer_Id(id)
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerAppointment(AppointmentRegisterDTO appointmentRegisterDTO){
        Clinic clinic = getClinicById(appointmentRegisterDTO.clinicId());
        Customer customer = getCustomerById(appointmentRegisterDTO.CustomerId());

        AppointmentCalendar reservation = AppointmentCalendar.builder()
                .clinic(clinic)
                .customer(customer)
                .reservation(appointmentRegisterDTO.reservation())
                .build();
        appointmentCalendarRepository.save(reservation);
        return ResponseEntity.ok("Customer registered successfully");
    }
    public ResponseEntity<String> deleteAppointmentById(long id){
        checkAppointmentExistenceById(id);
        appointmentCalendarRepository.deleteById(id);
        return  ResponseEntity.ok("Appointment deleted successfully");

    }
    private void checkAppointmentExistenceById(long id){
        appointmentCalendarRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment"));
    }
    private Clinic getClinicById(long id){
        return clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinic"));
    }
    private Customer getCustomerById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer"));
    }
}
