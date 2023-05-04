package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.repository.DentistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DentistService {
    private final DentistRepository dentistRepository;

}
