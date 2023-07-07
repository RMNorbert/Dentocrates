package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentCalendarService {
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    private final CustomerRepository customerRepository;
    private final ClinicRepository clinicRepository;
    @Autowired
    public AppointmentCalendarService(AppointmentCalendarRepository appointmentCalendarRepository, CustomerRepository customerRepository, ClinicRepository clinicRepository) {
        this.appointmentCalendarRepository = appointmentCalendarRepository;
        this.customerRepository = customerRepository;
        this.clinicRepository = clinicRepository;
    }

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
    public ResponseEntity<String> registerAppointment(AppointmentDTO appointmentDTO){
        Clinic clinic = getClinicById(appointmentDTO.clinicId());
        Customer customer = getCustomerById(appointmentDTO.customerId());

        AppointmentCalendar reservation = AppointmentCalendar.builder()
                .clinic(clinic)
                .customer(customer)
                .reservation(appointmentDTO.reservation())
                .build();
        appointmentCalendarRepository.save(reservation);
        return ResponseEntity.ok("Customer registered successfully");
    }
    public ResponseEntity<String> deleteAppointmentById(long id){
        checkAppointmentExistenceById(id);
        appointmentCalendarRepository.deleteById(id);
        return  ResponseEntity.ok("Appointment deleted successfully");

    }
    public List<AppointmentDTO> getAllAppointmentByClinic(long id) {
            return appointmentCalendarRepository.getAllByClinic_Id(id)
                    .stream()
                    .map(AppointmentDTO::of)
                    .toList();
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
