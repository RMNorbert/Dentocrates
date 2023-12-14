package org.rmnorbert.dentocrates.service.client.dentist;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import org.rmnorbert.dentocrates.repository.client.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.DELETE_RESPONSE_CONTENT;
import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.INVALID_REQUEST_RESPONSE_CONTENT;

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

    public ResponseEntity<String> deleteDentistById(DeleteDTO dto){
        DentistResponseDTO dentist = getDentistById(dto.targetId());
        if(dto.userId() == dentist.id()) {
            dentistRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Dentist" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + "delete dentist");
    }

    public DentistResponseDTO getDentistById(long id){
        return DentistResponseDTO.of(dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist")));
    }
    public Dentist getDentist(String email) {
        return dentistRepository.getClientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dentist"));
    }

    public Dentist saveDentist(Dentist dentist) {
        return dentistRepository.save(dentist);
    }
    public Dentist verifyDentist(String email){
        Dentist dentist = getDentist(email);
        dentist.setVerified(true);
        return saveDentist(dentist);
    }
    public Dentist updateDentistPassword(String email, String newPassword){
        Dentist dentist = getDentist(email);
        dentist.setPassword(newPassword);
        return saveDentist(dentist);
    }
}
