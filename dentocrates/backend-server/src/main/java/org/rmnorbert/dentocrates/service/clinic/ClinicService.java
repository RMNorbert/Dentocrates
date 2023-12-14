package org.rmnorbert.dentocrates.service.clinic;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.dao.location.Location;
import org.rmnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import org.rmnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import org.rmnorbert.dentocrates.repository.client.DentistRepository;
import org.rmnorbert.dentocrates.repository.clinic.ClinicRepository;
import org.rmnorbert.dentocrates.repository.clinic.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;

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
            return ResponseEntity.ok("Clinic" +  SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body("Location" + NOT_FOUND_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteClinicById(Long id){
        getClinicById(id);
        clinicRepository.deleteById(id);
        return  ResponseEntity.ok("Clinic" + DELETE_RESPONSE_CONTENT);

    }
    public ClinicResponseDTO getClinicById(long id){
        return ClinicResponseDTO.of(clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic")));
    }

    public List<ClinicResponseDTO> getAllClinicByDentist(long id) {
        return clinicRepository.findAllByDentistInContract_Id(id)
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }
}
