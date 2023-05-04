package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

}
