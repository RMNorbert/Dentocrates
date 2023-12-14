package org.rmnorbert.dentocrates.service.clinic.location;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.location.Location;
import org.rmnorbert.dentocrates.dto.location.LocationDTO;
import org.rmnorbert.dentocrates.repository.clinic.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.DELETE_RESPONSE_CONTENT;
import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.SUCCESSFUL_REGISTER_RESPONSE_CONTENT;

@Service
public class LocationService {
    private final static double MIN_LATITUDE = 47.43436780681293;
    private final static double MAX_LATITUDE = 47.6015897075242;
    private final static double MIN_LONGITUDE = 18.988484817303743;
    private final static double MAX_LONGITUDE = 19.149691360899396;
    private final Random random;
    private final LocationRepository locationRepository;
    @Autowired
    public LocationService(Random random, LocationRepository locationRepository) {
        this.random = random;
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocation(){
        return locationRepository.findAll()
                .stream()
                .map(LocationDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerLocation(LocationDTO locationDTO){
        double longitude = generateRandomCoordinates(MIN_LONGITUDE,MAX_LONGITUDE);
        double latitude = generateRandomCoordinates(MIN_LATITUDE,MAX_LATITUDE);
        Location location = Location.of(locationDTO,longitude,latitude);

        locationRepository.save(location);
        return ResponseEntity.ok("Location" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteLocationById(Long id){
        Location location = getLocationById(id);
        String locationName = location.getZipCode() + "-" +  location.getCity();
        locationRepository.deleteById(id);
        return  ResponseEntity.ok("Location: " + locationName + DELETE_RESPONSE_CONTENT);
    }
    private Location getLocationById(long id){
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location"));
    }
    private double generateRandomCoordinates(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}

