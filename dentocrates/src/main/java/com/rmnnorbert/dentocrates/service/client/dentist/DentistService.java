package com.rmnnorbert.dentocrates.service.client.dentist;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.repository.client.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class DentistService {
    private final DentistRepository dentistRepository;
    @Autowired
    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    public List<DentistResponseDTO> getAllDentist(){
        return dentistRepository.findAll()
                .stream()
                .map(DentistResponseDTO::of)
                .toList();
    }
    public DentistResponseDTO getDentistById(long id){
        return DentistResponseDTO.of(dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist")));
    }

    public ResponseEntity<String> deleteDentistById(DeleteDTO dto){
        DentistResponseDTO dentist = getDentistById(dto.targetId());
        if(dto.userId() == dentist.id()) {
            dentistRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Dentist" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + "delete dentist");
    }

    public Dentist saveDentist(Dentist dentist) {
        return dentistRepository.save(dentist);
    }

    /**
     * Verifies a dentist by setting the 'verified' flag to true.
     *
     * @param email The email of the dentist to be verified.
     * @return The Dentist entity after verification.
     */
    public Dentist verifyDentist(String email){
        Dentist dentist = getDentist(email);
        dentist.setVerified(true);
        return saveDentist(dentist);
    }

    /**
     * Updates the password for a dentist identified by the provided email.
     *
     * @param email       The email of the dentist whose password is to be updated.
     * @param newPassword The new password to set for the dentist.
     * @return The Dentist entity after updating the password.
     */
    public Dentist updateDentistPassword(String email, String newPassword){
        Dentist dentist = getDentist(email);
        dentist.setPassword(newPassword);
        return saveDentist(dentist);
    }
    private Dentist getDentist(String email) {
        return dentistRepository.getClientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dentist"));
    }
}
