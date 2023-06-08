package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicResponseDTO;
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
        if (clinicRepository.findByName(clinicRegisterDTO.name()).isPresent()) {
            return ResponseEntity.badRequest().body("Clinic already registered");
        }else {
            Dentist dentist = dentistRepository.getReferenceById(clinicRegisterDTO.dentistId());
            int locationZipCode = clinicRegisterDTO.location().getZipCode();
            Optional<Location> location =  locationRepository.getByZipCode(locationZipCode);
            if(location.isPresent()) {
                Clinic clinic = Clinic.of(clinicRegisterDTO, dentist, location.get());
                clinicRepository.save(clinic);
                return ResponseEntity.ok("Clinic registered successfully");
            }
            return ResponseEntity.badRequest().body("Invalid location.");
        }
    }
    public ResponseEntity<String> deleteClinicById(Long id){
        if(clinicRepository.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body("Clinic don't exist.");
        }else{
            clinicRepository.deleteById(id);
            return  ResponseEntity.ok("Clinic deleted successfully");
        }
    }

}
