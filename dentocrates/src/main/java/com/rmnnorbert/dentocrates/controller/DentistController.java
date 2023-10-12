package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.service.client.dentist.DentistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/dentist")
public class DentistController {
    private final DentistService dentistService;
    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/all")
    public List<DentistResponseDTO> getAllDentist(){
        return dentistService.getAllDentist();
    }

    @GetMapping("/{id}")
    public DentistResponseDTO getDentistById(@PathVariable Long id){
        return dentistService.getDentistById(id);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteDentist(@Valid @RequestBody DeleteDTO dto){
        return dentistService.deleteDentistById(dto);
    }
}
