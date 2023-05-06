package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.clinic.LocationDTO;
import com.rmnnorbert.dentocrates.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {
    private final LocationService locationService;
    @GetMapping("/all")
    public List<LocationDTO> getAllLocation(){
        return locationService.getAllLocation();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerLocation(@RequestBody LocationDTO locationDTO){
        return locationService.registerLocation(locationDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable long id){
        return locationService.deleteLocationById(id);
    }
}
