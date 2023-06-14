package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinic")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping("/all")
    public List<ClinicResponseDTO> getAllClinic(){
        return clinicService.getAllClinic();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerClinic(@Valid @RequestBody ClinicRegisterDTO clinicRegisterDTO){
        return clinicService.registerClinic(clinicRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable long id){
        return clinicService.deleteClinicById(id);
    }
}
