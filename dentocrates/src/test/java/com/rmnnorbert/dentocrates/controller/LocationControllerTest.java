package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.dto.clinic.location.LocationDTO;
import com.rmnnorbert.dentocrates.service.clinic.LocationService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {
    private LocationController locationController;
    @Mock
    private LocationService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.locationController = new LocationController(service);
    }
    @ParameterizedTest
    @MethodSource(value = "provideExpectedList")
    void getAllLocationShouldReturnExpectedList(List<LocationDTO> expected) {
        when(service.getAllLocation()).thenReturn(expected);

        List<LocationDTO> actual = locationController.getAllLocation();
        assertEquals(expected, actual);
        verify(service,times(1)).getAllLocation();
    }

    @Test
    void registerLocationShouldReturnExpectedValue() {
        LocationDTO dto = new LocationDTO(1000,"city");
        ResponseEntity<String> expected = ResponseEntity.ok("Location registered successfully");
        when(service.registerLocation(dto)).thenReturn(expected);

        ResponseEntity<String> actual = locationController.registerLocation(dto);
        assertEquals(expected,actual);
        verify(service,times(1)).registerLocation(dto);
    }
    @Test
    void deleteLocation() {
        long id = 1;
        ResponseEntity<String> expected = ResponseEntity.ok("Location deleted successfully");

        when(service.deleteLocationById(id)).thenReturn(expected);

        ResponseEntity<String> actual = locationController.deleteLocation(id);
        assertEquals(expected, actual);
        verify(service,times(1)).deleteLocationById(id);
    }
    private static Stream<Arguments> provideExpectedList() {
        return Stream.of(
                Arguments.of( List.of(new LocationDTO(1000,"city"))),
                Arguments.of( List.of())
        );
    }
}
