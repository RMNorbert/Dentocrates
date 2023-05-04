package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.DentistResponseDTO;
import com.rmnnorbert.dentocrates.service.DentistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dentist")
@AllArgsConstructor
public class DentistController {
    private final DentistService dentistService;

    @GetMapping("/all")
    public List<DentistResponseDTO> getAllDentist(){
        return dentistService.getAllDentist();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerDentist(@RequestBody DentistRegisterDTO dentistRegisterDTO){
        return dentistService.registerDentist(dentistRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDentist(@PathVariable Long id){
        return dentistService.deleteDentistById(id);
    }
}
