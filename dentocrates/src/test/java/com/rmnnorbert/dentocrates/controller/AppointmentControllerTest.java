package com.rmnnorbert.dentocrates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rmnnorbert.dentocrates.controller.clinic.AppointmentController;
import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentRegisterDTO;
import com.rmnnorbert.dentocrates.dto.appointment.AppointmentUpdateDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.service.clinic.calendar.AppointmentCalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {
    private AppointmentController appointmentController;
    private ObjectMapper objectMapper;
    @Mock
    private AppointmentCalendarService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.appointmentController = new AppointmentController(service);
    }
    @ParameterizedTest
    @MethodSource("provideIdAndExpectedList")
    void getCustomerAppointmentsShouldReturnExpectedList(Long id, List<AppointmentDTO> expected) {
        when(service.getAllAppointmentById(id)).thenReturn(expected);

        List<AppointmentDTO> actual = appointmentController.getCustomerAppointments(id);

        assertEquals(expected.size(),actual.size());
        verify(service,times(1)).getAllAppointmentById(id);
    }

    @Test
    void addCalendarShouldReturnOkStatusAndExpectedValue() {
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment registered successfully");
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(1, 1, LocalDateTime.now());

        when(service.registerAppointment(dto)).thenReturn(expected);

        ResponseEntity<String> actual = appointmentController.addCalendar(dto);

        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
        verify(service,times(1)).registerAppointment(dto);
    }
    @Test
    void addCalendarWithUnregisteredIdShouldReturnNotFoundStatusAndEmptyBody() {
        AppointmentRegisterDTO dto = new AppointmentRegisterDTO(2, 1, LocalDateTime.now());

        when(service.registerAppointment(dto)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> appointmentController.addCalendar(dto));
        verify(service,times(1)).registerAppointment(dto);
    }


    @Test
    void removeAppointmentShouldReturnOkStatusAndExpectedMessage() {
        DeleteDTO dto = new DeleteDTO(1,1);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment deleted successfully");
        when(service.deleteAppointmentById(dto)).thenReturn(expected);

        ResponseEntity<String> actual = appointmentController.removeAppointment(dto);

        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
        verify(service,times(1)).deleteAppointmentById(dto);
    }
    @Test
    void removeAppointmentShouldReturnBadRequestStatusAndExpectedMessage() {
        DeleteDTO dto = new DeleteDTO(1,1);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid delete request.");
        when(service.deleteAppointmentById(dto)).thenReturn(expected);

        ResponseEntity<String> actual = appointmentController.removeAppointment(dto);

        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        assertEquals(expected.getBody(),actual.getBody());
        verify(service,times(1)).deleteAppointmentById(dto);
    }
    @Test
    void removeAppointmentShouldReturnNotFoundException() {
        DeleteDTO dto = new DeleteDTO(1,1);

        when(service.deleteAppointmentById(dto)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> appointmentController.removeAppointment(dto));

        verify(service,times(1)).deleteAppointmentById(dto);
    }
    @ParameterizedTest
    @MethodSource("provideIdAndExpectedList")
    void getAppointmentsByClinic(Long id, List<AppointmentDTO> expected) {
        when(service.getAllAppointmentByClinic(id)).thenReturn(expected);

        List<AppointmentDTO> actual = appointmentController.getAppointmentsByClinic(id);

        assertEquals(expected.size(),actual.size());
        verify(service,times(1)).getAllAppointmentByClinic(id);
    }

    @Test
    void updateAppointmentShouldReturnOkStatusAndExpectedMessage() {
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(1,1,1,true);
        ResponseEntity<String> expected = ResponseEntity.ok("Appointment updated successfully");

        when(service.updateAppointment(dto)).thenReturn(expected);

        ResponseEntity<String> actual = appointmentController.updateAppointment(dto);

        assertEquals(expected,actual);
        verify(service,times(1)).updateAppointment(dto);
    }
    @Test
    void updateAppointmentShouldReturnBadRequestStatusAndExpectedMessage() {
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(1,1,1,true);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid update request.");

        when(service.updateAppointment(dto)).thenReturn(expected);

        ResponseEntity<String> actual = appointmentController.updateAppointment(dto);

        assertEquals(expected,actual);
        verify(service,times(1)).updateAppointment(dto);
    }

    @Test
    void updateAppointmentShouldReturnNotFoundException() {
        AppointmentUpdateDTO dto = new AppointmentUpdateDTO(1,1,1,true);

        when(service.updateAppointment(dto)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> appointmentController.updateAppointment(dto));

        verify(service,times(1)).updateAppointment(dto);
    }

    private static Stream<Arguments> provideIdAndExpectedList() {
        return Stream.of(
            Arguments.of(1L,List.of(
                    new AppointmentDTO(1,1,1,
                            LocalDateTime.now(),false,false))),
            Arguments.of(2L,List.of())
        );
    }
}
