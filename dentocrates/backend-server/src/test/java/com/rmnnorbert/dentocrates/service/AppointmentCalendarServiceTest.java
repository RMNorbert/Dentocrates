package com.rmnnorbert.dentocrates.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.appointmentCalendar.AppointmentCalendar;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentRegisterDTO;
import org.rmnorbert.dentocrates.dto.appointment.AppointmentUpdateDTO;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.repository.clinic.ClinicRepository;
import org.rmnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import org.rmnorbert.dentocrates.service.clinic.calendar.AppointmentCalendarService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppointmentCalendarServiceTest {
    private static long TEST_ID=1;
    @Mock
    private AppointmentCalendarRepository appointmentCalendarRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @InjectMocks
    private AppointmentCalendarService appointmentCalendarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAppointmentByIdWhenThereAreAppointments() {
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        AppointmentCalendar appointment2 = AppointmentCalendar.builder().id(2L).customer(customer).clinic(clinic).build();
        List<AppointmentCalendar> appointments = List.of(appointment1, appointment2);

        when(appointmentCalendarRepository.getAllByCustomer_Id(TEST_ID)).thenReturn(appointments);
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentById(TEST_ID);

        assertEquals(appointments.size(), result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
    }

    @Test
    void getAllAppointmentByIdWhenThereAreNoAppointments() {
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        List<AppointmentCalendar> appointments = new ArrayList<>();

        when(appointmentCalendarRepository.getAllByCustomer_Id(TEST_ID)).thenReturn(appointments);
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentById(TEST_ID);

        assertEquals(0, result.size());
    }

    @Test
    void registerAppointment() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));
        when(customerRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(customer));

        ResponseEntity<String> actual = appointmentCalendarService.registerAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment successfully registered");
        assertEquals(expected,actual);
    }
    @Test
    void registerAppointmentWhenClinicDoesNotExist() {
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.registerAppointment(dto));
    }
    @Test
    void registerAppointmentWhenCustomerDoesNotExist() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(TEST_ID,TEST_ID,LocalDateTime.now());

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.registerAppointment(dto));
    }
    @Test
    void deleteAppointmentById() {
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(TEST_ID,TEST_ID);
        when(appointmentCalendarRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentCalendarService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment deleted successfully");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongCustomerId() {
        long originalUserId = 2;
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(originalUserId,TEST_ID);
        when(appointmentCalendarRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentCalendarService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to  delete appointment");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongAppointmentId() {
        long userId = 2;
        DeleteDTO dto = new DeleteDTO(userId,TEST_ID);

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.deleteAppointmentById(dto));
        verify(appointmentCalendarRepository,times(1)).findById(TEST_ID);
    }
    @Test
    void getAllAppointmentByClinic() {
        long id2 = 1L;
        Customer customer = new Customer(TEST_ID, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Customer customer2 = new Customer(id2, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(TEST_ID).build();
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(TEST_ID).customer(customer).clinic(clinic).build();
        AppointmentCalendar appointment2 = AppointmentCalendar.builder().id(id2).customer(customer2).clinic(clinic).build();
        List<AppointmentCalendar> appointments = List.of(appointment1, appointment2);

        when(appointmentCalendarRepository.getAllByClinic_Id(TEST_ID)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentByClinic(TEST_ID);

        assertEquals(2, result.size());
    }
    @Test
    void getAllAppointmentByClinicWhenNoAppointmentExist() {
        long id = 1;
        List<AppointmentCalendar> appointments = new ArrayList<>();

        when(appointmentCalendarRepository.getAllByClinic_Id(id)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentByClinic(id);

        assertEquals(appointments.size(), result.size());
    }
    @Test
    void updateAppointmentWhenClinicExist(){
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(TEST_ID,TEST_ID,TEST_ID,false);
        Dentist dentist = Dentist.builder().id(TEST_ID).build();
        Customer customer = Customer.builder().build();
        Clinic clinic = Clinic.builder().id(TEST_ID).dentistInContract(dentist).build();
        AppointmentCalendar appointmentCalendar = AppointmentCalendar.builder().clinic(clinic).customer(customer).build();

        when(appointmentCalendarRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(appointmentCalendar));
        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentCalendarService.updateAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment updated successfully registered");

        assertEquals(expected, actual);
    }
    @Test
    void updateAppointmentWhenClinicDoesNotExist(){
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(TEST_ID,TEST_ID,TEST_ID,false);

        when(clinicRepository.findById(TEST_ID)).thenThrow(new NotFoundException("Clinic"));

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.updateAppointment(dto));
    }
    @Test
    void updateAppointmentWhenUserIdInvalid(){
        long id2 = 2;
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(id2,TEST_ID,id2,false);
        Dentist dentist2 = Dentist.builder().id(TEST_ID).build();
        Clinic clinic = Clinic.builder().id(TEST_ID).dentistInContract(dentist2).build();

        when(clinicRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentCalendarService.updateAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to  update appointment");

        assertEquals(expected, actual);
    }
}
