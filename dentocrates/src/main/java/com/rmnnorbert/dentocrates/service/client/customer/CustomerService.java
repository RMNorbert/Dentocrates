package com.rmnnorbert.dentocrates.service.client.customer;

import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.clinic.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.DELETE_RESPONSE_CONTENT;
import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.INVALID_REQUEST_RESPONSE_CONTENT;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository, AppointmentRepository appointmentRepository) {
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
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
            appointmentRepository.deleteById(dto.targetId());
            customerRepository.deleteById(dto.targetId());
            return ResponseEntity.ok("Customer" + DELETE_RESPONSE_CONTENT);
        }
        return ResponseEntity.badRequest().body(INVALID_REQUEST_RESPONSE_CONTENT + " delete customer");
    }
    public CustomerResponseDTO getCustomerResponse(long id){
        return CustomerResponseDTO.of(customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer")));
    }
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Verifies a customer by setting the 'verified' flag to true.
     *
     * @param email The email of the customer to be verified.
     * @return The Customer entity after verification.
     */
    public Customer verifyCustomer(String email){
        Customer customer = getCustomer(email);
        customer.setVerified(true);
        return saveCustomer(customer);
    }

    /**
     * Updates the password for a customer identified by the provided email.
     *
     * @param email       The email of the customer whose password is to be updated.
     * @param newPassword The new password to set for the customer.
     * @return The Customer entity after updating the password.
     */
    public Customer updateCustomerPassword(String email, String newPassword){
        Customer customer = getCustomer(email);
        customer.setPassword(newPassword);
        return saveCustomer(customer);
    }
    private Customer getCustomer(String email) {
        return customerRepository.getClientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    private Customer getClientById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer"));
    }
}
