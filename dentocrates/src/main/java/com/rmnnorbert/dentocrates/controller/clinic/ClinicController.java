package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.service.clinic.ClinicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
    private final ClinicService clinicService;
    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/all")
    public List<ClinicResponseDTO> getAllClinic(){
        return clinicService.getAllClinic();
    }
    @GetMapping("/dentist/{id}")
    public List<ClinicResponseDTO> getAllClinicByDentist(@PathVariable long id){
        return clinicService.getAllClinicByDentist(id);
    }

    @GetMapping("/{id}")
    public ClinicResponseDTO getClinicById(@PathVariable Long id){
        return clinicService.getClinicById(id);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerClinic(@Valid @RequestBody ClinicRegisterDTO clinicRegisterDTO){
        return clinicService.registerClinic(clinicRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable Long id){
        return clinicService.deleteClinicById(id);
    }
}
