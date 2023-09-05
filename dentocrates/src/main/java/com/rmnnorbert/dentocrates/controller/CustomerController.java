package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.service.client.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class CustomerController {
    private final CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public List<CustomerAppointmentResponseDTO> getAllCustomer(){
        return customerService.getAllCustomerWithAppointment();
    }
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomer(@PathVariable Long id){
        return customerService.getClient(id);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteCustomer(@Valid @RequestBody DeleteDTO dto){
        return customerService.deleteCustomerById(dto);
    }
}
