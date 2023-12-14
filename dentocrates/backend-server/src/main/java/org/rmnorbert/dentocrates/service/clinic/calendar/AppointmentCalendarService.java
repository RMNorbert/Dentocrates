package org.rmnorbert.dentocrates.service.clinic.calendar;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentRegisterDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentUpdateDTO;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.repository.clinic.ClinicRepository;
import org.rmnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;

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

    public List<AppointmentDTO> getAllAppointmentById(long id){
        return appointmentCalendarRepository.getAllByCustomer_Id(id)
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerAppointment(AppointmentRegisterDTO appointmentDTO){
        Clinic clinic = getClinicById(appointmentDTO.clinicId());
        Customer customer = getCustomerById(appointmentDTO.customerId());

        AppointmentCalendar reservation = AppointmentCalendar.builder()
                .clinic(clinic)
                .customer(customer)
                .reservation(appointmentDTO.reservation())
                .build();

        appointmentCalendarRepository.save(reservation);
        return ResponseEntity.ok("Appointment" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteAppointmentById(DeleteDTO dto){
        AppointmentCalendar appointmentCalendar = getAppointmentById(dto.targetId());
        if(dto.userId() == appointmentCalendar.getCustomer().getId()) {
            appointmentCalendarRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Appointment" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete appointment");
    }
    public List<AppointmentDTO> getAllAppointmentByClinic(long id) {
            return appointmentCalendarRepository.getAllByClinic_Id(id)
                    .stream()
                    .map(AppointmentDTO::of)
                    .toList();
    }

    public ResponseEntity<String> updateAppointment(AppointmentUpdateDTO dto) {
        Clinic clinic = getClinicById(dto.clinicId());
        if(dto.dentistId() == clinic.getDentistInContract().getId()) {
            AppointmentCalendar appointment = getAppointmentById(dto.id()).withAppeared(dto.appeared());
            appointmentCalendarRepository.save(appointment);
            return ResponseEntity.ok("Appointment updated" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " update appointment");
    }

    private Clinic getClinicById(long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
    private Customer getCustomerById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    private AppointmentCalendar getAppointmentById(long id) {
        return appointmentCalendarRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment"));
    }

    public ResponseEntity<String> updateReviewStateOfAppointment(Long id) {
        try {
        AppointmentCalendar appointment = appointmentCalendarRepository.getReferenceById(id)
                .withReviewed(true);

        appointmentCalendarRepository.save(appointment);
        return ResponseEntity.ok("Appointment review state change" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        } catch (Exception e) {
            throw new NotFoundException("Appointment");
        }
    }
}
