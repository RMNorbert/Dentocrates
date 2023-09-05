package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.appointment.AppointmentUpdateDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.AppointmentCalendar;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.service.clinic.AppointmentCalendarService;
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

class AppointmentCalendarServiceTest {
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
        long customerId = 1L;
        Customer customer = new Customer(customerId, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(1L).build();
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(1L).customer(customer).clinic(clinic).build();
        AppointmentCalendar appointment2 = AppointmentCalendar.builder().id(2L).customer(customer).clinic(clinic).build();
        List<AppointmentCalendar> appointments = List.of(appointment1, appointment2);

        // Mock the behavior of the repositories
        when(appointmentCalendarRepository.getAllByCustomer_Id(customerId)).thenReturn(appointments);
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));

        // Call the service method
        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentById(customerId);

        // Verify the expected behavior
        assertEquals(appointments.size(), result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        // Verify that the repository methods were called
        verify(appointmentCalendarRepository, times(1)).getAllByCustomer_Id(customerId);
    }

    @Test
    void getAllAppointmentByIdWhenThereAreNoAppointments() {
        long customerId = 1L;
        Customer customer = new Customer(customerId, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        List<AppointmentCalendar> appointments = new ArrayList<>();

        // Mock the behavior of the repositories
        when(appointmentCalendarRepository.getAllByCustomer_Id(customerId)).thenReturn(appointments);
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));

        // Call the service method
        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentById(customerId);

        // Verify the expected behavior
        assertEquals(0, result.size());
        // Verify that the repository methods were called
        verify(appointmentCalendarRepository, times(1)).getAllByCustomer_Id(customerId);

    }

    @Test
    void registerAppointment() {
        long id = 1;
        Clinic clinic = Clinic.builder().id(id).build();
        Customer customer = new Customer(id, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(id,id,LocalDateTime.now());

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));
        when(customerRepository.findById(id)).thenReturn(java.util.Optional.of(customer));

        ResponseEntity<String> actual = appointmentCalendarService.registerAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment registered successfully");
        assertEquals(expected,actual);
    }
    @Test
    void registerAppointmentWhenClinicDoesNotExist() {
        long id = 1;
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(id,id,LocalDateTime.now());

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.registerAppointment(dto));
        verify(clinicRepository,times(1)).findById(dto.clinicId());
    }
    @Test
    void registerAppointmentWhenCustomerDoesNotExist() {
        long id = 1;
        Clinic clinic = Clinic.builder().id(id).build();
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(id,id,LocalDateTime.now());

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.registerAppointment(dto));
        verify(clinicRepository,times(1)).findById(dto.clinicId());
        verify(customerRepository,times(1)).findById(id);
    }
    @Test
    void deleteAppointmentById() {
        long id = 1;
        Clinic clinic = Clinic.builder().id(id).build();
        Customer customer = new Customer(id, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(id).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(id,id);
        when(appointmentCalendarRepository.findById(id)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentCalendarService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment deleted successfully");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongCustomerId() {
        long id = 1;
        long originalUserId = 2;
        Clinic clinic = Clinic.builder().id(id).build();
        Customer customer = new Customer(id, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(id).customer(customer).clinic(clinic).build();
        DeleteDTO dto = new DeleteDTO(originalUserId,id);
        when(appointmentCalendarRepository.findById(id)).thenReturn(Optional.ofNullable(appointment1));

        ResponseEntity<String> actual = appointmentCalendarService.deleteAppointmentById(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid delete request.");

        assertEquals(expected,actual);
    }
    @Test
    void deleteAppointmentByIdWithWrongAppointmentId() {
        long id = 1;
        long userId = 2;
        DeleteDTO dto = new DeleteDTO(userId,id);

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.deleteAppointmentById(dto));
        verify(appointmentCalendarRepository,times(1)).findById(id);
    }
    @Test
    void getAllAppointmentByClinic() {
        long id = 1L;
        long id2 = 1L;
        Customer customer = new Customer(id, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Customer customer2 = new Customer(id2, "email@example.com", "password", "John", "Doe", Role.CUSTOMER);
        Clinic clinic = Clinic.builder().id(id).build();
        AppointmentCalendar appointment1 = AppointmentCalendar.builder().id(id).customer(customer).clinic(clinic).build();
        AppointmentCalendar appointment2 = AppointmentCalendar.builder().id(id2).customer(customer2).clinic(clinic).build();
        List<AppointmentCalendar> appointments = List.of(appointment1, appointment2);

        // Mock the behavior of the repositories
        when(appointmentCalendarRepository.getAllByClinic_Id(id)).thenReturn(appointments);

        // Call the service method
        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentByClinic(id);

        // Verify the expected behavior
        assertEquals(2, result.size());
        // Verify that the repository methods were called
        verify(appointmentCalendarRepository, times(1)).getAllByClinic_Id(id);
    }
    @Test
    void getAllAppointmentByClinicWhenNoAppointmentExist() {
        long id = 1;
        List<AppointmentCalendar> appointments = new ArrayList<>();

        when(appointmentCalendarRepository.getAllByClinic_Id(id)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentCalendarService.getAllAppointmentByClinic(id);

        assertEquals(appointments.size(), result.size());

        verify(appointmentCalendarRepository, times(1)).getAllByClinic_Id(id);
    }
    @Test
    void updateAppointmentWhenClinicExist(){
        long id = 1;
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(id,id,id,false);
        Dentist dentist = Dentist.builder().id(id).build();
        Customer customer = Customer.builder().build();
        Clinic clinic = Clinic.builder().id(id).dentistInContract(dentist).build();
        AppointmentCalendar appointmentCalendar = AppointmentCalendar.builder().clinic(clinic).customer(customer).build();

        when(appointmentCalendarRepository.findById(id)).thenReturn(Optional.ofNullable(appointmentCalendar));
        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentCalendarService.updateAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment updated successfully");

        assertEquals(expected, actual);
        verify(clinicRepository,times(1)).findById(id);
        verify(appointmentCalendarRepository,times(1)).save(appointmentCalendar);
    }
    @Test
    void updateAppointmentWhenClinicDoesNotExist(){
        long id = 1;
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(id,id,id,false);

        when(clinicRepository.findById(id)).thenThrow(new NotFoundException("Clinic"));

        assertThrows(NotFoundException.class, () -> appointmentCalendarService.updateAppointment(dto));
        verify(clinicRepository,times(1)).findById(id);
    }
    @Test
    void updateAppointmentWhenUserIdInvalid(){
        long id = 1;
        long id2 = 2;
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(id2,id,id2,false);
        Dentist dentist = Dentist.builder().id(id2).build();
        Dentist dentist2 = Dentist.builder().id(id).build();
        Clinic clinic = Clinic.builder().id(id).dentistInContract(dentist2).build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = appointmentCalendarService.updateAppointment(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid update request.");

        assertEquals(expected, actual);
        verify(clinicRepository,times(1)).findById(id);
    }
}
