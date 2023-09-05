package com.rmnnorbert.dentocrates.service.client;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
            return ResponseEntity.ok("Dentist deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid delete request.");
    }

    public DentistResponseDTO getDentistById(long id){
        return DentistResponseDTO.of(dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist")));
    }

}
