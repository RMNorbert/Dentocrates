package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DentistService {
    private final DentistRepository dentistRepository;

    public List<DentistResponseDTO> getAllDentist(){
        return dentistRepository.findAll()
                .stream()
                .map(DentistResponseDTO::of)
                .toList();
    }

    public ResponseEntity<String> deleteDentistById(Long id){
        Dentist dentist = getDentistById(id);
        dentistRepository.deleteById(id);
        return  ResponseEntity.ok("Dentist deleted successfully");

    }
    private Dentist getDentistById(long id){
        return dentistRepository.findById(id).orElseThrow(() -> new NotFoundException("Dentist"));
    }
}
