package com.rmnnorbert.dentocrates.service;

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

    public ResponseEntity<String> deleteDentistById(Long id){
        DentistResponseDTO dentist = getDentistById(id);
        dentistRepository.deleteById(id);
        return  ResponseEntity.ok("Dentist deleted successfully");
    }

    public DentistResponseDTO getDentistById(long id){
        return DentistResponseDTO.of(dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist")));
    }

}
