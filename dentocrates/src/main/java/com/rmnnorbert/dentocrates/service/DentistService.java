package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.client.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.DentistResponseDTO;
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
    public ResponseEntity<String> registerDentist(DentistRegisterDTO dentistRegisterDTO){
        if (dentistRepository.findByEmail(dentistRegisterDTO.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }else {
            Dentist dentist = Dentist.of(dentistRegisterDTO);
            dentistRepository.save(dentist);

            return ResponseEntity.ok("Dentist registered successfully");
        }
    }
    public ResponseEntity<String> deleteDentistById(Long id){
        if(dentistRepository.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body("Dentist don't exist.");
        }else{
            dentistRepository.deleteById(id);
            return  ResponseEntity.ok("Dentist deleted successfully");
        }
    }

}
