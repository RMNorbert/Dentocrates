package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.clinic.location.LocationDTO;
import com.rmnnorbert.dentocrates.service.clinic.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/all")
    public List<LocationDTO> getAllLocation(){
        return locationService.getAllLocation();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerLocation(@Valid @RequestBody LocationDTO locationDTO){
        return locationService.registerLocation(locationDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id){
        return locationService.deleteLocationById(id);
    }
}
