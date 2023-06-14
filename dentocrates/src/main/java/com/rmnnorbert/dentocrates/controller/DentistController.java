package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistResponseDTO;
import com.rmnnorbert.dentocrates.service.DentistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/dentist")
public class DentistController {
    private final DentistService dentistService;

    @GetMapping("/all")
    public List<DentistResponseDTO> getAllDentist(){
        return dentistService.getAllDentist();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerDentist(@Valid @RequestBody DentistRegisterDTO dentistRegisterDTO){
        return dentistService.registerDentist(dentistRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDentist(@PathVariable long id){
        return dentistService.deleteDentistById(id);
    }
}
