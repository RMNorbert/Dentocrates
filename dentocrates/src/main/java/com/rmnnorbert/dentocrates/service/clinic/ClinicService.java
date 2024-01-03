package com.rmnnorbert.dentocrates.service.clinic;

import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicUpdateDTO;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.client.DentistRepository;
import com.rmnnorbert.dentocrates.repository.clinic.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

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
    public ClinicResponseDTO getClinicResponseDTOById(long id){
        return ClinicResponseDTO.of(getClinicById(id));
    }

    public List<ClinicResponseDTO> getAllClinicByDentist(long id) {
        return clinicRepository.findAllByDentistInContract_Id(id)
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }

    public ResponseEntity<String> updateClinicById(Long id, ClinicUpdateDTO dto) {
        Optional<Location> location = locationRepository.getByZipCode(dto.zipCode());

        if(location.isPresent()) {
            Clinic clinic = getClinicById(id)
                    .withName(dto.name())
                    .withWebsite(dto.website())
                    .withContactNumber(dto.contactNumber())
                    .withLocation(location.get())
                    .withStreet(dto.street())
                    .withOpeningHours(dto.openingHours());

            clinicRepository.save(clinic);
            return ResponseEntity.ok("Clinic" + SUCCESSFUL_UPDATE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body("Location" + NOT_FOUND_RESPONSE_CONTENT);
    }

    private Clinic getClinicById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
}
