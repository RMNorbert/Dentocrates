package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.location.LocationDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.clinic.Location;
import com.rmnnorbert.dentocrates.repository.LocationRepository;
import com.rmnnorbert.dentocrates.service.clinic.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private LocationService locationService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocationWhenLocationsExists() {
        long id = 1;
        long id2 = 2;
        Location location = Location.builder().id(id).city("city").build();
        Location location2 = Location.builder().id(id2).city("city2").build();
        List<Location> locations = List.of(location,location2);

        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationDTO> actual = locationService.getAllLocation();

        assertEquals(locations.size(), actual.size());
        assertEquals(locations.get(0).getCity(), actual.get(0).city());
        verify(locationRepository, times(1)).findAll();
    }
    @Test
    void getAllLocationWhenLocationsDoNotExists() {
        List<Location> locations = new ArrayList<>();

        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationDTO> actual = locationService.getAllLocation();

        assertEquals(locations.size(), actual.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void registerLocation() {
        LocationDTO dto = new LocationDTO(1000,"City");

        ResponseEntity<String> actual = locationService.registerLocation(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Location registered successfully");

        assertEquals(expected, actual);
    }

    @Test
    void deleteLocationByIdWhenLocationExist() {
        long id = 1;
        Location location = Location.builder().id(id).build();

        when(locationRepository.findById(id)).thenReturn(Optional.ofNullable(location));

        ResponseEntity<String> actual = locationService.deleteLocationById(id);
        ResponseEntity<String> expected = ResponseEntity.ok("Location deleted successfully");

        assertEquals(expected, actual);
        verify(locationRepository,times(1)).findById(id);
        verify(locationRepository, times(1)).deleteById(id);
    }
    @Test
    void deleteLocationByIdWhenLocationDoNotExist() {
        long id = 1;

        assertThrows(NotFoundException.class, () -> locationService.deleteLocationById(id));

        verify(locationRepository,times(1)).findById(id);
        verify(locationRepository, times(0)).deleteById(id);
    }
}
