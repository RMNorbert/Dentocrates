package com.rmnnorbert.dentocrates.service.client;

import com.rmnnorbert.dentocrates.controller.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.repository.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository, AppointmentCalendarRepository appointmentCalendarRepository) {
        this.customerRepository = customerRepository;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
    }

    public List<CustomerAppointmentResponseDTO> getAllCustomerWithAppointment(){
        return customerRepository.findAll()
                .stream()
                .map(CustomerAppointmentResponseDTO::toDTO)
                .toList();
    }

    public ResponseEntity<String> deleteCustomerById(DeleteDTO dto){
        Customer customer = getClientById(dto.targetId());
        if(dto.userId() == customer.getId()) {
            appointmentCalendarRepository.deleteById(dto.targetId());
            customerRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Customer deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid delete request.");
    }
    public CustomerResponseDTO getClient(long id){
        return CustomerResponseDTO.of(customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer")));
    }
    private Customer getClientById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer"));
    }

}
