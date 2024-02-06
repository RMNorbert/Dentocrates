package com.rmnnorbert.dentocrates.service.clinic;

import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dao.location.Location;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicUpdateDTO;
import com.rmnnorbert.dentocrates.repository.client.DentistRepository;
import com.rmnnorbert.dentocrates.repository.clinic.ClinicRepository;
import com.rmnnorbert.dentocrates.repository.clinic.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final DentistRepository dentistRepository;
    private final LocationRepository locationRepository;
    @Autowired
    public ClinicService(ClinicRepository clinicRepository,
                         DentistRepository dentistRepository,
                         LocationRepository locationRepository) {
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

    public ClinicResponseDTO getClinicResponseDTOById(long id){
        return ClinicResponseDTO.of(findClinicById(id));
    }

    public List<ClinicResponseDTO> getAllClinicByDentist(long id) {
        return clinicRepository.findAllByDentistInContract_Id(id)
                .stream()
                .map(ClinicResponseDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerClinic(ClinicRegisterDTO clinicRegisterDTO){
        Clinic clinic = createClinicFromDTO(clinicRegisterDTO);
        clinicRepository.save(clinic);
        return ResponseEntity.ok("Clinic" +  SUCCESSFUL_REGISTER_RESPONSE_CONTENT);
    }
    public ResponseEntity<String> deleteClinicById(Long id){
        findClinicById(id);
        clinicRepository.deleteById(id);
        return  ResponseEntity.ok("Clinic" + DELETE_RESPONSE_CONTENT);
    }

    /**
     * Updates a clinic based on the information provided in the ClinicUpdateDTO.
     *
     * @param dto The ClinicUpdateDTO containing the data for the clinic update.
     * @return ResponseEntity with a success message if the update is successful, or a bad request with an error message
     * if the specified dentist is not found in the contract.
     */
    public ResponseEntity<String> updateClinicById(ClinicUpdateDTO dto) {
        Clinic updatedClinic = getUpdatedClinic(dto);

        if(updatedClinic.getDentistInContract().getId().equals(dto.dentistId())) {
            clinicRepository.save(updatedClinic);
            return ResponseEntity.ok("Clinic" + SUCCESSFUL_UPDATE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body("Clinic" + NOT_FOUND_RESPONSE_CONTENT);
    }


    private Clinic findClinicById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clinic"));
    }
    private Dentist findDentistById(Long id) {
        return dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist"));
    }
    private Location findLocationByZipCode(int zipCode) {
        return locationRepository.getByZipCode(zipCode)
                .orElseThrow(() -> new NotFoundException("Location"));
    }
    /**
     * Creates a Clinic entity based on the information provided in the ClinicRegisterDTO.
     *
     * @param clinicRegisterDTO The ClinicRegisterDTO containing the data for clinic registration.
     * @return A Clinic entity with associated Dentist and Location entities.
     */
    private Clinic createClinicFromDTO(ClinicRegisterDTO clinicRegisterDTO) {
        Dentist dentist = findDentistById(clinicRegisterDTO.dentistId());
        Location location = findLocationByZipCode(clinicRegisterDTO.zipCode());
        return Clinic.of(clinicRegisterDTO, dentist, location);
    }

    /**
     * Retrieves an existing Clinic based on the ID from the ClinicUpdateDTO and updates its attributes with
     * the information provided in the DTO.
     *
     * @param dto The ClinicUpdateDTO containing the updated data for the clinic.
     * @return An updated Clinic entity with modified attributes.
     */
    private Clinic getUpdatedClinic(ClinicUpdateDTO dto) {
        Location location = findLocationByZipCode(dto.zipCode());

        return findClinicById(dto.clinicId())
                .withName(dto.name())
                .withWebsite(dto.website())
                .withContactNumber(dto.contactNumber())
                .withLocation(location)
                .withStreet(dto.street())
                .withOpeningHours(dto.openingHours());
    }
}
