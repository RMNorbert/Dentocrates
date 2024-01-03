package com.rmnnorbert.dentocrates.controller;

import com.rmnnorbert.dentocrates.controller.client.customer.CustomerController;
import com.rmnnorbert.dentocrates.dto.DeleteDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.service.client.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    private CustomerController customerController;
    @Mock
    private CustomerService customerService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.customerController = new CustomerController(customerService);
    }

    @ParameterizedTest
    @MethodSource(value = "provideExpectedList")
    void getAllCustomer(List<CustomerAppointmentResponseDTO> expected) {
        when(customerService.getAllCustomer()).thenReturn(expected);

        List<CustomerAppointmentResponseDTO> actual = customerController.getAllCustomer();

        assertEquals(expected,actual);
    }

    @Test
    void getCustomerShouldReturnExpectedValue() {
        long searchedId = 1;
        CustomerResponseDTO expected = new CustomerResponseDTO(1,"email","fisrt","last", Role.CUSTOMER,false);

        when(customerService.getCustomerResponse(searchedId)).thenReturn(expected);

        CustomerResponseDTO actual = customerController.getCustomer(searchedId);

        assertEquals(expected, actual);
    }
    @Test
    void getCustomerShouldReturnNotFoundException() {
        long searchedId = 1;

        when(customerService.getCustomerResponse(searchedId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> customerController.getCustomer(searchedId));
    }

    @ParameterizedTest
    @MethodSource(value = "provideExpectedDeleteDTOAndExpectedResponse")
    void deleteCustomerShouldReturnExpectedStatusAndMessage(DeleteDTO dto, ResponseEntity<String> expected) {
        when(customerService.deleteCustomerById(dto)).thenReturn(expected);

        ResponseEntity<String> actual = customerController.deleteCustomer(dto);
        assertEquals(expected, actual);
    }
    @Test
    void deleteCustomerShouldReturnNotFoundException() {
        DeleteDTO dto = new DeleteDTO(1,1);
        when(customerService.getCustomerResponse(dto.targetId())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> customerController.getCustomer(dto.targetId()));
    }
    private static Stream<Arguments> provideExpectedList() {
        return Stream.of(
                Arguments.of( List.of(new CustomerAppointmentResponseDTO(1L,
                        "email",
                        "first"))),
                Arguments.of(List.of())
        );
    }
    private static Stream<Arguments> provideExpectedDeleteDTOAndExpectedResponse() {
        return Stream.of(
                Arguments.of( new DeleteDTO(1,1), ResponseEntity.ok("Customer deleted successfully")),
                Arguments.of( new DeleteDTO(1,1), ResponseEntity.ok("Customer deleted successfully"))
        );
    }
}
