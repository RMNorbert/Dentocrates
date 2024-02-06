package com.rmnnorbert.dentocrates.service.clinic.location;

import com.rmnnorbert.dentocrates.dto.location.LocationDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.repository.clinic.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class LocationService {
    /** To optimize Mapbox API usage:
     * Default range values are used to provide a random sample coordinate in the area of Budapest for displaying
     * clinics on the map.
     * These default values can be replaced with actual coordinates fetched from the provided location.
     */
    private final static double MIN_LATITUDE = 47.43436780681293;
    private final static double MAX_LATITUDE = 47.6015897075242;
    private final static double MIN_LONGITUDE = 18.988484817303743;
    private final static double MAX_LONGITUDE = 19.149691360899396;
    private final Random random;
    private final LocationRepository locationRepository;
    @Autowired
    public LocationService(Random random,
                           LocationRepository locationRepository) {
        this.random = random;
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocation(){
        return locationRepository.findAll()
                .stream()
                .map(LocationDTO::of)
                .toList();
    }

    /**
     * Registers a new location based on the information provided in the LocationDTO.
     * Generates random coordinates for the location's longitude and latitude.
     *
     * @param locationDTO The LocationDTO containing the data for location registration.
     * @return ResponseEntity with a success message if the registration is successful.
     */
    public ResponseEntity<String> registerLocation(LocationDTO locationDTO){
        double longitude = generateRandomCoordinates(MIN_LONGITUDE,MAX_LONGITUDE);
        double latitude = generateRandomCoordinates(MIN_LATITUDE,MAX_LATITUDE);
        Location location = Location.of(locationDTO,longitude,latitude);

        locationRepository.save(location);
        return ResponseEntity.ok("Location" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteLocationById(Long id){
        Location location = getLocationById(id);
        locationRepository.deleteById(id);
        String locationName = location.getZipCode() + "-" +  location.getCity();
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

