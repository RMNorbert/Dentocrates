package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.dto.client.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.CustomerResponseDTO;
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
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerRegisterDTO customerRegisterDTO){
        return customerService.registerCustomer(customerRegisterDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomerById(id);
    }
}
