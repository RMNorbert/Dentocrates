package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.clinic.Leave;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.LeaveRepository;
import com.rmnnorbert.dentocrates.service.clinic.LeaveService;
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

class LeaveServiceTest {
    @Mock
    private LeaveRepository leaveRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @InjectMocks
    private LeaveService leaveService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllLeavesFromDateOfClinicWhenLeavesExist() {
        long id = 1;
        long id2 = 2;
        Clinic clinic = Clinic.builder().id(id).build();
        Leave leave = Leave.builder()
                .id(id)
                .startOfTheLeave(LocalDateTime.now())
                .endOfTheLeave(LocalDateTime.now())
                .clinic(clinic)
                .build();
        Leave leave2 = Leave.builder()
                .id(id2)
                .startOfTheLeave(LocalDateTime.now())
                .endOfTheLeave(LocalDateTime.now())
                .clinic(clinic)
                .build();
        List<Leave> leaveList = List.of(leave, leave2);

        when(leaveRepository.getAllByClinic_Id(id)).thenReturn(leaveList);

        List<LeaveDTO> actual = leaveService.getAllLeavesFromDateOfClinic(id);

        assertEquals(leaveList.size(), actual.size());
        verify(leaveRepository,times(1)).getAllByClinic_Id(id);
    }
    @Test
    void getAllLeavesFromDateOfClinicWhenLeavesDoNotExist() {
        long id = 1;
        List<Leave> leaveList = new ArrayList<>();

        when(leaveRepository.getAllByClinic_Id(id)).thenReturn(leaveList);

        List<LeaveDTO> actual = leaveService.getAllLeavesFromDateOfClinic(id);

        assertEquals(leaveList.size(), actual.size());
        verify(leaveRepository,times(1)).getAllByClinic_Id(id);
    }
    @Test
    void registerLeave() {
        long id = 1;
        LeaveRegisterDTO dto = new LeaveRegisterDTO(id,LocalDateTime.now(),LocalDateTime.now());
        Clinic clinic = Clinic.builder()
                .id(id)
                .build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = leaveService.registerLeave(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Leave registered successfully");

        assertEquals(expected, actual);

        verify(clinicRepository,times(1)).findById(id);
    }
    @Test
    void registerLeaveWithInvalidClinicId() {
        long id = 1;
        LeaveRegisterDTO dto = new LeaveRegisterDTO(id,LocalDateTime.now(),LocalDateTime.now());

        when(clinicRepository.findById(id)).thenThrow(new NotFoundException("Clinic"));

        assertThrows(NotFoundException.class, () -> leaveService.registerLeave(dto));

        verify(clinicRepository,times(1)).findById(id);
    }


    @Test
    void deleteLeave() {
        long id = 1;
        LeaveDeleteDTO dto = new LeaveDeleteDTO(id,id,id);
        Dentist dentist = Dentist.builder()
                .id(id)
                .build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .dentistInContract(dentist)
                .build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = leaveService.deleteLeave(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Leave deleted successfully");

        assertEquals(expected, actual);

        verify(clinicRepository,times(1)).findById(id);
        verify(leaveRepository,times(1)).deleteById(dto.leaveId());
    }
    @Test
    void deleteLeaveWithInvalidClinicId() {
        long id = 1;
        LeaveDeleteDTO dto = new LeaveDeleteDTO(id,id,id);

        assertThrows(NotFoundException.class, () -> leaveService.deleteLeave(dto));

        verify(clinicRepository,times(1)).findById(id);
        verify(leaveRepository,times(0)).deleteById(dto.leaveId());
    }
    @Test
    void deleteLeaveWithInvalidDentistId() {
        long id = 1;
        long id2 = 2;
        LeaveDeleteDTO dto = new LeaveDeleteDTO(id,id,id);
        Dentist dentist = Dentist.builder()
                .id(id2)
                .build();
        Clinic clinic = Clinic.builder()
                .id(id)
                .dentistInContract(dentist)
                .build();

        when(clinicRepository.findById(id)).thenReturn(Optional.ofNullable(clinic));

        ResponseEntity<String> actual = leaveService.deleteLeave(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid delete request.");

        assertEquals(expected, actual);

        verify(clinicRepository,times(1)).findById(id);
        verify(leaveRepository,times(0)).deleteById(dto.leaveId());
    }
}
