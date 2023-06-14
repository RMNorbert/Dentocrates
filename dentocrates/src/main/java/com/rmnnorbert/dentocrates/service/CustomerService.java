package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    public List<CustomerResponseDTO> getAllCustomer(){
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponseDTO::of)
                .toList();
    }
    public ResponseEntity<String> registerCustomer(CustomerRegisterDTO customerRegisterDTO){
        Customer customer = Customer.of(customerRegisterDTO);
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
    }
    public ResponseEntity<String> deleteCustomerById(Long id){
        Customer customer = getClientById(id);
        appointmentCalendarRepository.deleteById(id);
        customerRepository.deleteById(id);
        return  ResponseEntity.ok("Customer deleted successfully");
    }
    public CustomerResponseDTO getClient(long id){
        return CustomerResponseDTO.of(customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer")));
    }
    private Customer getClientById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer"));
    }

}
