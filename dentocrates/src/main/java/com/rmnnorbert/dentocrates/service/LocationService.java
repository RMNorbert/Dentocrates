package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.LocationDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.clinic.Location;
import com.rmnnorbert.dentocrates.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public List<LocationDTO> getAllLocation(){
        return locationRepository.findAll()
                .stream()
                .map(LocationDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerLocation(LocationDTO locationDTO){
        Location location = Location.of(locationDTO);
        locationRepository.save(location);
        return ResponseEntity.ok("Location registered successfully");
    }
    public ResponseEntity<String> deleteLocationById(Long id){
        Location location = getLocationById(id);
        locationRepository.deleteById(id);
        return  ResponseEntity.ok("Location deleted successfully");
    }
    private Location getLocationById(long id){
        return locationRepository.findById(id).orElseThrow(() -> new NotFoundException("Location"));
    }
}

