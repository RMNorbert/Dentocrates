package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.clinic.LeaveController;
import com.rmnnorbert.dentocrates.dto.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.service.clinic.leave.LeaveService;
import com.rmnnorbert.dentocrates.service.client.communicationServices.AppointmentNotificationService;
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
class LeaveControllerTest {
    private LeaveController leaveController;
    @Mock
    private LeaveService leaveService;
    @Mock
    private AppointmentNotificationService notificationService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.leaveController = new LeaveController(leaveService, notificationService);
    }
    @ParameterizedTest
    @MethodSource(value = "provideExpectedList")
    void getLeavesFromDateOfClinicShouldReturnExpectedList(long id , List<LeaveDTO> expected) {
        when(leaveService.getAllLeavesFromDateOfClinic(id)).thenReturn(expected);

        List<LeaveDTO> actual = leaveController.getLeavesFromDateOfClinic(id);
        assertEquals(expected, actual);
        verify(leaveService,times(1)).getAllLeavesFromDateOfClinic(id);
    }

    @Test
    void registerLeaveToClinicShouldReturnOkStatusAndExpectedMessage() {
        LeaveRegisterDTO dto = new LeaveRegisterDTO(1,LocalDateTime.now(),LocalDateTime.now());
        ResponseEntity<String> expected = ResponseEntity.ok("Leave registered successfully");
        when(leaveService.registerLeave(dto)).thenReturn(expected);

        ResponseEntity<String> actual = leaveController.registerLeaveToClinic(dto);
        assertEquals(expected, actual);
        verify(leaveService,times(1)).registerLeave(dto);
    }
    @Test
    void registerLeaveToClinicShouldReturnNotFoundException() {
        LeaveRegisterDTO dto = new LeaveRegisterDTO(1,LocalDateTime.now(),LocalDateTime.now());

        when(leaveService.registerLeave(dto)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> leaveController.registerLeaveToClinic(dto));
        verify(leaveService,times(1)).registerLeave(dto);
    }
    @ParameterizedTest
    @MethodSource(value = "provideDeleteDTOAndExpectedResponse")
    void deleteLeaveOfClinic(LeaveDeleteDTO dto, ResponseEntity<String> expected) {
        when(leaveService.deleteLeave(dto)).thenReturn(expected);

        ResponseEntity<String> actual = leaveController.deleteLeaveOfClinic(dto);
        assertEquals(expected, actual);
        verify(leaveService,times(1)).deleteLeave(dto);
    }
    private static Stream<Arguments> provideExpectedList() {
        return Stream.of(
                Arguments.of(1, List.of(new LeaveDTO(1,
                        1,
                        LocalDateTime.now(),
                        LocalDateTime.now()))),
                Arguments.of(2,List.of())
        );
    }
    private static Stream<Arguments> provideDeleteDTOAndExpectedResponse() {
        return Stream.of(
                Arguments.of( new LeaveDeleteDTO(1,
                        1,
                        1
                        ),
                        ResponseEntity.ok("Leave deleted successfully")),
                Arguments.of(new LeaveDeleteDTO(1,
                        1,
                        1
                ),ResponseEntity.badRequest().body("Invalid delete request."))
        );
    }
}
