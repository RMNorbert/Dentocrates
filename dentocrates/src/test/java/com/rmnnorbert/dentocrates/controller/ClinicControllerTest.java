package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.service.clinic.ClinicService;
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

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicControllerTest {
    private ClinicController clinicController;
    @Mock
    private ClinicService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.clinicController = new ClinicController(service);
    }
    @ParameterizedTest
    @MethodSource("provideExpectedList")
    void getAllClinicShouldReturnExpectedClinicList(List<ClinicResponseDTO> expected) {
        when(service.getAllClinic()).thenReturn(expected);

        List<ClinicResponseDTO> actual = clinicController.getAllClinic();

        assertEquals(expected, actual);
        verify(service,times(1)).getAllClinic();
    }

    @ParameterizedTest
    @MethodSource("provideIdAndExpectedList")
    void getAllClinicByDentistShouldReturnExpectedClinicList(Long id , List<ClinicResponseDTO> expected) {
        when(service.getAllClinicByDentist(id)).thenReturn(expected);

        List<ClinicResponseDTO> actual = clinicController.getAllClinicByDentist(id);

        assertEquals(expected,actual);
        verify(service,times(1)).getAllClinicByDentist(id);
    }

    @Test
    void getClinicByIdShouldReturnExpectedClinic() {
        long searchedId = 1;
        ClinicResponseDTO expected = new ClinicResponseDTO(1L,"name", "COMMUNITY_DENTAL_CLINIC","contact","site",1000,"street","6-2","1",1);

        when(service.getClinicById(searchedId)).thenReturn(expected);

        ClinicResponseDTO actual = clinicController.getClinicById(searchedId);

        assertEquals(expected,actual);
        verify(service,times(1)).getClinicById(searchedId);
    }
    @Test
    void getClinicByIdShouldReturnNotFoundException() {
        long searchedId = 1;

        when(service.getClinicById(searchedId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> clinicController.getClinicById(searchedId));
        verify(service,times(1)).getClinicById(searchedId);
    }
    @Test
    void registerClinicShouldReturnOkStatusAndExpectedMessage() {
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name", "Clinic","123123","site",1000,"city","street","6-2",1);
        ResponseEntity<String> expected = ResponseEntity.ok("Clinic registered successfully");
        when(service.registerClinic(dto)).thenReturn(expected);

        ResponseEntity<String> actual = clinicController.registerClinic(dto);
        assertEquals(expected,actual);
        verify(service,times(1)).registerClinic(dto);
    }
    @Test
    void registerClinicShouldReturnBadRequestAndExpectedMessage() {
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name", "Clinic","123123","site",1000,"city","street","6-2",1);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid location.");
        when(service.registerClinic(dto)).thenReturn(expected);

        ResponseEntity<String> actual = clinicController.registerClinic(dto);
        assertEquals(expected,actual);
        verify(service,times(1)).registerClinic(dto);
    }
    @Test
    void registerClinicWithWrongDentistIdShouldReturnNotFoundException() {
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name", "Clinic","123123","site",1000,"city","street","6-2",1);

        when(service.registerClinic(dto)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> clinicController.registerClinic(dto));
        verify(service,times(1)).registerClinic(dto);
    }
    @Test
    void registerClinicWithWrongLocationShouldReturnBadRequestWithExpectedMessage() {
        ClinicRegisterDTO dto = new ClinicRegisterDTO("name", "Clinic","123123","site",1000,"city","street","6-2",1);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid location.");
        when(service.registerClinic(dto)).thenReturn(expected);

        ResponseEntity<String> actual = clinicController.registerClinic(dto);
        assertEquals(expected, actual);
        verify(service,times(1)).registerClinic(dto);
    }


    @Test
    void deleteClinicShouldReturnOkStatusAndExpectedMessage() {
        long id = 1;
        ResponseEntity<String> expected = ResponseEntity.ok("Clinic deleted successfully");

        when(service.deleteClinicById(id)).thenReturn(expected);

        ResponseEntity<String> actual = clinicController.deleteClinic(id);
        assertEquals(expected, actual);
        verify(service,times(1)).deleteClinicById(id);
    }
    @Test
    void deleteClinicShouldReturnNotFoundException() {
        long id = 1;

        when(service.deleteClinicById(id)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> clinicController.deleteClinic(id));
        verify(service,times(1)).deleteClinicById(id);
    }
    private static Stream<Arguments> provideExpectedList() {
        return Stream.of(
                Arguments.of( List.of(new ClinicResponseDTO(1,"name", "PRIVATE_DENTAL_CLINIC","site","a232323",10000,"city","street","1-2",1))),
                Arguments.of(List.of())
        );
    }
    private static Stream<Arguments> provideIdAndExpectedList() {
        return Stream.of(
                Arguments.of(1L, List.of(new ClinicResponseDTO(1,"name", "PRIVATE_DENTAL_CLINIC","site","a232323",10000,"city","street","1-2",1))),
                Arguments.of(2L,List.of())
        );
    }
}
