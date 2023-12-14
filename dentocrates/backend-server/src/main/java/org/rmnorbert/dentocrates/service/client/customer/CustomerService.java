package org.rmnorbert.dentocrates.service.client.customer;

import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.DELETE_RESPONSE_CONTENT;
import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.INVALID_REQUEST_RESPONSE_CONTENT;

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
