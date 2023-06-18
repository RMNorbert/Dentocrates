package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping("/all")
    public List<CustomerResponseDTO> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomer(@PathVariable Long id){
        return customerService.getClient(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomerById(id);
    }
}
