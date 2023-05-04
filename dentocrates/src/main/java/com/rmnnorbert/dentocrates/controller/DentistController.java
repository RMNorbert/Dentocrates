package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.DentistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dentist")
@AllArgsConstructor
public class DentistController {
    private final DentistService dentistService;

}
