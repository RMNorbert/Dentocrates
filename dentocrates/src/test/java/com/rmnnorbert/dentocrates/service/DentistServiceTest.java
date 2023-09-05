package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.service.client.DentistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DentistServiceTest {
    @Mock
    private DentistRepository dentistRepository;
    @InjectMocks
    private DentistService dentistService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDentistWhenDentistsExist() {
        long id = 1;
        long id2 = 2;
        Dentist dentist = Dentist.builder().id(id).build();
        Dentist dentist2 = Dentist.builder().id(id2).build();
        List<Dentist> dentistList = List.of(dentist,dentist2);

        when(dentistRepository.findAll()).thenReturn(dentistList);

        List<DentistResponseDTO> actual = dentistService.getAllDentist();

        assertEquals(dentistList.size(), actual.size());
        assertEquals(dentistList.get(0).getId(), actual.get(0).id());
        assertEquals(dentistList.get(1).getId(), actual.get(1).id());
        verify(dentistRepository,times(1)).findAll();
    }

    @Test
    void getAllDentistWhenDentistsDoNotExist() {
        List<Dentist> dentistList = new ArrayList<>();

        when(dentistRepository.findAll()).thenReturn(dentistList);

        List<DentistResponseDTO> actual = dentistService.getAllDentist();

        assertEquals(dentistList.size(), actual.size());
        verify(dentistRepository,times(1)).findAll();
    }
    @Test
    void deleteDentistByIdWhenDentistExist() {
        long id = 1;
        Dentist dentist = Dentist.builder()
                .id(id)
                .build();
        DeleteDTO dto = new DeleteDTO(id,id);
        when(dentistRepository.findById(id)).thenReturn(Optional.ofNullable(dentist));

        ResponseEntity<String> actual = dentistService.deleteDentistById(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Dentist deleted successfully");

        assertEquals(expected,actual);
        verify(dentistRepository,times(1)).findById(id);
        verify(dentistRepository,times(1)).deleteById(id);

    }
    @Test
    void deleteDentistByIdWhenDentistIdInvalid() {
        long id = 1;
        long targetId = 2;
        Dentist dentist = Dentist.builder()
                .id(targetId)
                .build();
        DeleteDTO dto = new DeleteDTO(id,targetId);
        when(dentistRepository.findById(dto.targetId())).thenReturn(Optional.ofNullable(dentist));

        ResponseEntity<String> actual = dentistService.deleteDentistById(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid delete request.");

        assertEquals(expected,actual);
        verify(dentistRepository,times(1)).findById(dto.targetId());
        verify(dentistRepository,times(0)).deleteById(dto.targetId());
    }
    @Test
    void deleteDentistByIdWhenDentistDoNotExist() {
        long id = 1;

        assertThrows(NotFoundException.class, () -> dentistService.getDentistById(id));

        verify(dentistRepository,times(1)).findById(id);
        verify(dentistRepository,times(0)).deleteById(id);
    }
    @Test
    void getDentistByIdWhenDentistExist() {
        long id = 1;
        Dentist dentist = Dentist.builder()
                .id(id)
                .email("email")
                .password("password")
                .firstName("first")
                .lastName("last")
                .role(Role.DENTIST)
                .operatingLicenceNo("opLicense1")
                .build();
        when(dentistRepository.findById(id)).thenReturn(Optional.ofNullable(dentist));

        DentistResponseDTO actual = dentistService.getDentistById(id);
        DentistResponseDTO expected = DentistResponseDTO.of(dentist);

        assertEquals(expected,actual);
        verify(dentistRepository,times(1)).findById(id);
    }
    @Test
    void getDentistByIdWhenDentistDoNotExist() {
        long id = 1;

        assertThrows(NotFoundException.class, () -> dentistService.getDentistById(id));

        verify(dentistRepository,times(1)).findById(id);
    }
}
