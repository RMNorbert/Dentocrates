package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.dto.location.LocationDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.repository.clinic.location.LocationRepository;
import com.rmnnorbert.dentocrates.service.clinic.location.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private Random random;
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
    }
    @Test
    void getAllLocationWhenLocationsDoNotExists() {
        List<Location> locations = new ArrayList<>();

        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationDTO> actual = locationService.getAllLocation();

        assertEquals(locations.size(), actual.size());
    }

    @Test
    void registerLocation() {
        LocationDTO dto = new LocationDTO(1000,"City",0,0);
        ResponseEntity<String> actual = locationService.registerLocation(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Location successfully registered");

        assertEquals(expected, actual);
    }

    @Test
    void deleteLocationByIdWhenLocationExist() {
        long id = 1;
        Location location = Location.builder().id(id).city("City").zipCode(1500).build();

        when(locationRepository.findById(id)).thenReturn(Optional.ofNullable(location));

        ResponseEntity<String> actual = locationService.deleteLocationById(id);
        ResponseEntity<String> expected = ResponseEntity.ok("Location: 1500-City deleted successfully");

        assertEquals(expected, actual);
    }
    @Test
    void deleteLocationByIdWhenLocationDoNotExist() {
        long id = 1;
        assertThrows(NotFoundException.class, () -> locationService.deleteLocationById(id));
    }
}
