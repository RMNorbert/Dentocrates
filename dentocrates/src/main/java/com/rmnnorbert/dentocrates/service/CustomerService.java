package com.rmnnorbert.dentocrates.service;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.customExceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    public List<CustomerAppointmentResponseDTO> getAllCustomer(){
        return customerRepository.findAll()
                .stream()
                .map(CustomerAppointmentResponseDTO::toDTO)
                .toList();
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
