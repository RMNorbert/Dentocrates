package com.rmnnorbert.dentocrates.service.clinic.location;

import com.rmnnorbert.dentocrates.dto.clinic.location.LocationDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.clinic.Location;
import com.rmnnorbert.dentocrates.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

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
        String locationName = location.getZipCode() + "-" +  location.getCity();
        locationRepository.deleteById(id);
        return  ResponseEntity.ok("Location: " + locationName + " deleted successfully");
    }
    private Location getLocationById(long id){
        return locationRepository.findById(id).orElseThrow(() -> new NotFoundException("Location"));
    }
}

