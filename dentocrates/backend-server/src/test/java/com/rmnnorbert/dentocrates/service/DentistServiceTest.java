package com.rmnnorbert.dentocrates.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import org.rmnorbert.dentocrates.repository.client.DentistRepository;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
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
    }

    @Test
    void getAllDentistWhenDentistsDoNotExist() {
        List<Dentist> dentistList = new ArrayList<>();

        when(dentistRepository.findAll()).thenReturn(dentistList);

        List<DentistResponseDTO> actual = dentistService.getAllDentist();

        assertEquals(dentistList.size(), actual.size());
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
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to delete dentist");

        assertEquals(expected,actual);
    }
    @Test
    void deleteDentistByIdWhenDentistDoNotExist() {
        long id = 1;

        assertThrows(NotFoundException.class, () -> dentistService.getDentistById(id));
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
    }
    @Test
    void getDentistByIdWhenDentistDoNotExist() {
        long id = 1;

        assertThrows(NotFoundException.class, () -> dentistService.getDentistById(id));
    }
}
