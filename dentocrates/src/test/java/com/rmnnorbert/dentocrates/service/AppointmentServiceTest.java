package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentUpdateDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.appointment.Appointment;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.repository.clinic.appointment.AppointmentRepository;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import com.rmnnorbert.dentocrates.service.clinic.calendar.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    private static final long TEST_ID=1;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAppointmentByIdWhenThereAreAppointments() {
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Appointment appointment1 = Appointment.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        Appointment appointment2 = Appointment.builder().id(2L).customer(customer).clinic(clinic).build();
        List<Appointment> appointments = List.of(appointment1, appointment2);

        when(appointmentRepository.getAllByCustomer_Id(TEST_ID)).thenReturn(appointments);
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        List<AppointmentDTO> result = appointmentService.getAllAppointmentById(TEST_ID);

        assertEquals(appointments.size(), result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
    }

    @Test
    void getAllAppointmentByIdWhenThereAreNoAppointments() {
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        List<Appointment> appointments = new ArrayList<>();

        when(appointmentRepository.getAllByCustomer_Id(TEST_ID)).thenReturn(appointments);
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        List<AppointmentDTO> result = appointmentService.getAllAppointmentById(TEST_ID);

        assertEquals(0, result.size());
    }

    @Test
    void registerAppointment() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        ResponseEntity<String> actual = appointmentService.registerAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment successfully registered");
        assertEquals(expected,actual);
    }
    @Test
    void registerAppointmentWhenClinicDoesNotExist() {
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        assertThrows(NotFoundException.class, () -> appointmentService.registerAppointment(dto));
    }
    @Test
    void registerAppointmentWhenCustomerDoesNotExist() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        assertThrows(NotFoundException.class, () -> appointmentService.registerAppointment(dto));
    }
    @Test
    void deleteAppointmentById() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Appointment appointment1 = Appointment.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(TEST_ID,TEST_ID);
        when(appointmentRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment deleted successfully");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongCustomerId() {
        long originalUserId = 2;
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Appointment appointment1 = Appointment.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(originalUserId,TEST_ID);
        when(appointmentRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to  delete appointment");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongAppointmentId() {
        long userId = 2;
        DeleteDTO dto = new DeleteDTO(userId,TEST_ID);

        assertThrows(NotFoundException.class, () -> appointmentService.deleteAppointmentById(dto));
        verify(appointmentRepository,times(1)).findById(TEST_ID);
    }
    @Test
    void getAllAppointmentByClinic() {
        long id2 = 1L;
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Customer customer2 = new Customer(id2, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Appointment appointment1 = Appointment.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        Appointment appointment2 = Appointment.builder().id(id2).customer(customer2).clinic(clinic).build();
        List<Appointment> appointments = List.of(appointment1, appointment2);

        when(appointmentRepository.getAllByClinic_Id(TEST_ID)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.getAllAppointmentByClinic(TEST_ID);

        assertEquals(2, result.size());
    }
    @Test
    void getAllAppointmentByClinicWhenNoAppointmentExist() {
        long id = 1;
        List<Appointment> appointments = new ArrayList<>();

        when(appointmentRepository.getAllByClinic_Id(id)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.getAllAppointmentByClinic(id);

        assertEquals(appointments.size(), result.size());
    }
    @Test
    void updateAppointmentWhenClinicExist(){
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(TEST_ID,TEST_ID,TEST_ID,false);
        Dentist dentist = Dentist.builder().id(TEST_ID).build();
        Customer customer = Customer.builder().build();
        Clinic clinic = Clinic.builder().id(TEST_ID).dentistInContract(dentist).build();
        Appointment appointment = Appointment.builder().clinic(clinic).customer(customer).build();

        when(appointmentRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointment));
        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentService.updateAppearanceOnAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment updated successfully registered");

        assertEquals(expected, actual);
    }
    @Test
    void updateAppointmentWhenClinicDoesNotExist(){
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(TEST_ID,TEST_ID,TEST_ID,false);

        when(clinicRepository.findById(TEST_ID)).thenThrow(new NotFoundException("Clinic"));

        assertThrows(NotFoundException.class, () -> appointmentService.updateAppearanceOnAppointment(dto));
    }
    @Test
    void updateAppointmentWhenUserIdInvalid(){
        long id2 = 2;
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(id2,TEST_ID,id2,false);
        Dentist dentist2 = Dentist.builder().id(TEST_ID).build();
        Clinic clinic = Clinic.builder().id(TEST_ID).dentistInContract(dentist2).build();

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentService.updateAppearanceOnAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to  update appointment");

        assertEquals(expected, actual);
    }
}
