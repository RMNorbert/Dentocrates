package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.service.client.DentistService;
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
        verify(dentistService,times(1)).getAllDentist();
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
        verify(dentistService,times(1)).getDentistById(searchedId);
    }
    @Test
    void getDentistByIdShouldReturnNotFoundException() {
        long searchedId = 1;
        when(dentistService.getDentistById(searchedId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> dentistController.getDentistById(searchedId));
        verify(dentistService,times(1)).getDentistById(searchedId);
    }
    @ParameterizedTest
    @MethodSource(value = "provideDeleteDTOAndExpectedResponse")
    void deleteDentist(DeleteDTO dto, ResponseEntity<String> expected) {
        when(dentistService.deleteDentistById(dto)).thenReturn(expected);

        ResponseEntity<String> actual = dentistController.deleteDentist(dto);
        assertEquals(expected, actual);
        verify(dentistService,times(1)).deleteDentistById(dto);
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
