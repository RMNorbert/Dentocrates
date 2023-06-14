package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.service.CustomerService;
import jakarta.validation.Valid;
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
    public CustomerResponseDTO getAllCustomer(@PathVariable long id){
        return customerService.getClient(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegisterDTO customerRegisterDTO){
        return customerService.registerCustomer(customerRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long id){
        return customerService.deleteCustomerById(id);
    }
}
