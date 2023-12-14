package com.rmnnorbert.dentocrates.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rmnorbert.dentocrates.custom.exceptions.NotFoundException;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.repository.clinic.appointmentCalendar.AppointmentCalendarRepository;
import org.rmnorbert.dentocrates.service.client.customer.CustomerService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AppointmentCalendarRepository appointmentCalendarRepository;
    @InjectMocks
    private CustomerService customerService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomerWithAppointmentWhenCustomersExist() {
        long id = 1;
        long id2 = 2;
        Customer customer = Customer.builder().id(id).build();
        Customer customer2 = Customer.builder().id(id2).build();
        List<Customer> customerList = List.of(customer,customer2);

        when(customerRepository.findAll()).thenReturn(customerList);

        List<CustomerAppointmentResponseDTO> actual = customerService.getAllCustomer();

        assertEquals(customerList.size(), actual.size());
        assertEquals(customerList.get(0).getId(), actual.get(0).id());
    }
    @Test
    void getAllCustomerWithAppointmentWhenCustomersDoesNotExist() {
        List<Customer> customerList = new ArrayList<>();

        when(customerRepository.findAll()).thenReturn(customerList);

        List<CustomerAppointmentResponseDTO> actual = customerService.getAllCustomer();

        assertEquals(customerList.size(), actual.size());
    }

    @Test
    void deleteCustomerByIdWithValidId() {
        long id = 1;
        Customer customer = Customer.builder().id(id).build();
        DeleteDTO dto = new DeleteDTO(id,id);

        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));

        ResponseEntity<String> actual = customerService.deleteCustomerById(dto);
        ResponseEntity<String> expected = ResponseEntity.ok("Customer deleted successfully");

        assertEquals(expected, actual);
    }
    @Test
    void deleteCustomerByIdWithInvalidId() {
        long invalidId = 1;
        long id = 2;
        Customer customer = Customer.builder().id(id).build();
        DeleteDTO dto = new DeleteDTO(invalidId,id);

        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));

        ResponseEntity<String> actual = customerService.deleteCustomerById(dto);
        ResponseEntity<String> expected = ResponseEntity.badRequest().body("Invalid request to  delete customer");

        assertEquals(expected, actual);
    }
    @Test
    void deleteCustomerByIdWithInvalidTargetId() {
        long id = 1;
        long invalidTargetId = 2;
        DeleteDTO dto = new DeleteDTO(id,invalidTargetId);

        assertThrows(NotFoundException.class, () -> customerService.deleteCustomerById(dto));
    }
    @Test
    void getClientWhenClientWithIdExist() {
        long id = 1;
        Customer customer = Customer.builder()
                .id(id)
                .email("email")
                .password("password")
                .firstName("first")
                .lastName("last")
                .role(Role.CUSTOMER)
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));

        CustomerResponseDTO actual = customerService.getCustomerResponse(id);
        CustomerResponseDTO expected = CustomerResponseDTO.of(customer);

        assertEquals(expected, actual);
    }
    @Test
    void getClientWhenClientWithIdDoNotExist() {
        long id = 1;

        when(customerRepository.findById(id)).thenThrow(new NotFoundException("Customer"));

        assertThrows(NotFoundException.class, () -> customerService.getCustomerResponse(id));
    }
}
