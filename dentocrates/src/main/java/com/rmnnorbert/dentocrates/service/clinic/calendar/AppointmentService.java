package com.rmnnorbert.dentocrates.service.clinic.calendar;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentUpdateDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.appointment.Appointment;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.repository.clinic.appointment.AppointmentRepository;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ClinicRepository clinicRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              CustomerRepository customerRepository,
                              ClinicRepository clinicRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.clinicRepository = clinicRepository;
    }

    public List<AppointmentDTO> getAllAppointmentById(long id){
        return appointmentRepository.getAllByCustomer_Id(id)
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }

    public List<AppointmentDTO> getAllAppointmentByClinic(long id) {
        return appointmentRepository.getAllByClinic_Id(id)
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }

    public List<AppointmentDTO> getAllAppointmentsForTheWeekByClinic(long id) {
        return appointmentRepository.getTheNextSevenDaysAppointmentsByClinicId(id)
                .stream()
                .map(AppointmentDTO::of)
                .toList();
    }

    public ResponseEntity<String> registerAppointment(AppointmentRegisterDTO appointmentDTO){
        Appointment reservation = createAppointmentFromDTO(appointmentDTO);
        appointmentRepository.save(reservation);
        return ResponseEntity.ok("Appointment" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteAppointmentById(DeleteDTO dto){
        Appointment appointment = getAppointmentById(dto.targetId());
        if(dto.userId() == appointment.getCustomer().getId()) {
            appointmentRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Appointment" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete appointment");
    }

    /**
     * Updates the appearance status for an appointment based on the information provided in the AppointmentUpdateDTO.
     *
     * @param dto The AppointmentUpdateDTO containing the data for updating the appointment appearance status.
     * @return ResponseEntity with a success message if the update is successful, or a bad request with an error message
     * if the specified dentist is not associated with the clinic.
     */
    public ResponseEntity<String> updateAppearanceOnAppointment(AppointmentUpdateDTO dto) {
        Clinic clinic = getClinicById(dto.clinicId());

        if (dto.dentistId() == clinic.getDentistInContract().getId()) {
            Appointment appointment = getAppointmentById(dto.id()).withAppeared(dto.appeared());

            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment updated" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " update appointment");
    }

    /**
     * Updates the review state of an appointment, marking it as reviewed.
     *
     * @param id The ID of the appointment to be updated.
     * @return ResponseEntity with a success message if the update is successful.
     * @throws NotFoundException If the appointment with the specified ID is not found.
     */
    public ResponseEntity<String> updateReviewStateOfAppointment(Long id) {
        try {
            Appointment appointment = appointmentRepository.getReferenceById(id)
                    .withReviewed(true);

            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment review state change" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        } catch (Exception e) {
            throw new NotFoundException("Appointment");
        }
    }

    private Clinic getClinicById(long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
    private Customer getCustomerById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    private Appointment getAppointmentById(long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment"));
    }

    /**
     * Creates an Appointment entity based on the information provided in the AppointmentRegisterDTO.
     *
     * @param appointmentDTO The AppointmentRegisterDTO containing the data for appointment registration.
     * @return An Appointment entity with associated Clinic and Customer entities.
     */
    private Appointment createAppointmentFromDTO(AppointmentRegisterDTO appointmentDTO) {
        Clinic clinic = getClinicById(appointmentDTO.clinicId());
        Customer customer = getCustomerById(appointmentDTO.customerId());
        return Appointment.builder()
                .clinic(clinic)
                .customer(customer)
                .reservation(appointmentDTO.reservation())
                .build();
    }
}
