package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.clinic.Location;
import com.rmnnorbert.dentocrates.repository.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final DentistRepository dentistRepository;
    private final LocationRepository locationRepository;
    @Autowired
    public ClinicService(ClinicRepository clinicRepository, DentistRepository dentistRepository, LocationRepository locationRepository) {
        this.clinicRepository = clinicRepository;
        this.dentistRepository = dentistRepository;
        this.locationRepository = locationRepository;
    }

    public List<ClinicResponseDTO> getAllClinic(){
        return clinicRepository.findAll()
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerClinic(ClinicRegisterDTO clinicRegisterDTO){
        Dentist dentist = dentistRepository.getReferenceById(clinicRegisterDTO.dentistId());
        if(dentist == null) {
            throw new NotFoundException("Dentist");
        }

        int locationZipCode = clinicRegisterDTO.zipCode();
        Optional<Location> location = locationRepository.getByZipCode(locationZipCode);

        if (location.isPresent()) {
            Clinic clinic = Clinic.of(clinicRegisterDTO, dentist, location.get());
            clinicRepository.save(clinic);
            return ResponseEntity.ok("Clinic registered successfully");
        }
        return ResponseEntity.badRequest().body("Invalid location.");
    }
    public ResponseEntity<String> deleteClinicById(Long id){
        getClinicById(id);
        clinicRepository.deleteById(id);
        return  ResponseEntity.ok("Clinic deleted successfully");

    }
    public ClinicResponseDTO getClinicById(long id){
        return ClinicResponseDTO.of(clinicRepository.findById(id).orElseThrow(() -> new NotFoundException("Clinic")));
    }

    public List<ClinicResponseDTO> getAllClinicByDentist(long id) {
        return clinicRepository.findAllByDentistInContract_Id(id)
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }
}
