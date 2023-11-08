package com.rmnnorbert.dentocrates.controller.client.customer;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.service.client.customer.CustomerService;
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
        return customerService.getCustomerResponse(id);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteCustomer(@Valid @RequestBody DeleteDTO dto){
        return customerService.deleteCustomerById(dto);
    }
}
