package com.rmnnorbert.dentocrates.service.client.customer;

import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentCalendarRepository appointmentCalendarRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository, AppointmentCalendarRepository appointmentCalendarRepository) {
        this.customerRepository = customerRepository;
        this.appointmentCalendarRepository = appointmentCalendarRepository;
    }

    public List<CustomerAppointmentResponseDTO> getAllCustomer(){
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
            return ResponseEntity.ok("Customer" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete customer");
    }
    public CustomerResponseDTO getCustomerResponse(long id){
        return CustomerResponseDTO.of(customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer")));
    }

    private Customer getClientById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer"));
    }
    public Customer getCustomer(String email) {
        return customerRepository.getClientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    public Customer verifyCustomer(String email){
        Customer customer = getCustomer(email);
        customer.setVerified(true);
        return saveCustomer(customer);
    }
    public Customer updateCustomerPassword(String email, String newPassword){
        Customer customer = getCustomer(email);
        customer.setPassword(newPassword);
        return saveCustomer(customer);
    }

}
