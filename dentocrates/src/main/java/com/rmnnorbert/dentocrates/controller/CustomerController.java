package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class CustomerController {
    private final CustomerService customerService;

}
