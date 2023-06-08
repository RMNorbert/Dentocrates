package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.client.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerResponseDTO> getAllCustomer(){
        return customerRepository.findAll()
                                 .stream()
                                 .map(CustomerResponseDTO::of)
                                 .toList();
    }
    public ResponseEntity<String> registerCustomer(CustomerRegisterDTO customerRegisterDTO){
        if (customerRepository.findByEmail(customerRegisterDTO.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }else {
            Customer customer = Customer.of(customerRegisterDTO);
            customerRepository.save(customer);

            return ResponseEntity.ok("Customer registered successfully");
        }
    }
    public ResponseEntity<String> deleteCustomerById(Long id){
        if(customerRepository.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body("Customer with  don't exist.");
        }else{
            customerRepository.deleteById(id);
            return  ResponseEntity.ok("Customer deleted successfully");
        }
    }

}
