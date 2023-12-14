package com.rmnnorbert.dentocrates.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rmnorbert.dentocrates.controller.client.dentist.DentistController;
import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistControllerTest {
    private DentistController dentistController;
    @Mock
    private DentistService dentistService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.dentistController = new DentistController(dentistService);
    }
    @ParameterizedTest
    @MethodSource("provideExpectedList")
    void getAllDentistShouldReturnExpectedList(List<DentistResponseDTO> expected) {
        when(dentistService.getAllDentist()).thenReturn(expected);

        List<DentistResponseDTO> actual = dentistController.getAllDentist();
        assertEquals(expected,actual);
    }

    @Test
    void getDentistByIdShouldReturnExpectedValue() {
        long searchedId = 1;
        DentistResponseDTO expected = new DentistResponseDTO(1,
                "email",
                "first",
                "last",
                Role.DENTIST,
                "op");
        when(dentistService.getDentistById(searchedId)).thenReturn(expected);

        DentistResponseDTO actual = dentistController.getDentistById(searchedId);
        assertEquals(expected, actual);
    }
    @Test
    void getDentistByIdShouldReturnNotFoundException() {
        long searchedId = 1;
        when(dentistService.getDentistById(searchedId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> dentistController.getDentistById(searchedId));
    }
    @ParameterizedTest
    @MethodSource(value = "provideDeleteDTOAndExpectedResponse")
    void deleteDentist(DeleteDTO dto, ResponseEntity<String> expected) {
        when(dentistService.deleteDentistById(dto)).thenReturn(expected);

        ResponseEntity<String> actual = dentistController.deleteDentist(dto);
        assertEquals(expected, actual);
    }
    private static Stream<Arguments> provideExpectedList() {
        return Stream.of(
                Arguments.of( List.of(new DentistResponseDTO(1,
                        "email",
                        "first",
                         "last",
                           Role.DENTIST,
                    "op")),
                Arguments.of(List.of()))
        );
    }
    private static Stream<Arguments> provideDeleteDTOAndExpectedResponse() {
        return Stream.of(
                Arguments.of( new DeleteDTO(1, 1), ResponseEntity.ok("Dentist deleted successfully")),
                Arguments.of(new DeleteDTO(1, 1),ResponseEntity.badRequest().body("Invalid delete request."))
        );
    }

}
