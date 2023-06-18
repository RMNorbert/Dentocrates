package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.clinic.Location;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final DentistRepository dentistRepository;
    private final LocationRepository locationRepository;
    public List<ClinicResponseDTO> getAllClinic(){
        return clinicRepository.findAll()
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerClinic(ClinicRegisterDTO clinicRegisterDTO){
        Dentist dentist = dentistRepository.getReferenceById(clinicRegisterDTO.dentistId());
        int locationZipCode = clinicRegisterDTO.zipCode();
        Optional<Location> location =  locationRepository.getByZipCode(locationZipCode);
        if(location.isPresent()) {
            Clinic clinic = Clinic.of(clinicRegisterDTO, dentist, location.get());
            clinicRepository.save(clinic);
            return ResponseEntity.ok("Clinic registered successfully");
        }
        return ResponseEntity.badRequest().body("Invalid location.");
    }
    public ResponseEntity<String> deleteClinicById(Long id){
        ClinicResponseDTO clinic = getClinicById(id);
        clinicRepository.deleteById(id);
        return  ResponseEntity.ok("Clinic deleted successfully");

    }
    public ClinicResponseDTO getClinicById(long id){
        return ClinicResponseDTO.of(clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinic")));
    }
}
