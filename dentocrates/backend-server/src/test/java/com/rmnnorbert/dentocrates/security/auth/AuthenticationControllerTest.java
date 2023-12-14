package com.rmnnorbert.dentocrates.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rmnorbert.dentocrates.controller.client.authentication.AuthenticationController;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import org.rmnorbert.dentocrates.security.auth.AuthenticationService;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    private static long USER_ID = 0;
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationService service;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.authenticationController = new AuthenticationController(service);
    }
    @Test
    void registerCustomerShouldReturnOkStatusAndExpectedValue() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email@e.com",
                                                       "password",
                                                       "name",
                                                       "last");
        Boolean expectedResponseBody = true;

        when(service.register(dto)).thenReturn(expectedResponseBody);

        ResponseEntity<Boolean> actual = authenticationController.register(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertEquals(Boolean.TRUE, actual.getBody());
    }
    @Test
    void registerCustomerShouldReturnOkStatusWithNoBody() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email@e.com",
                                                       "password",
                                                       "name",
                                                        "last");

        ResponseEntity<Boolean> actual = authenticationController.register(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertTrue(actual.hasBody());
    }
    @Test
    void registerDentistShouldReturnOkStatusAndExpectedValue() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");
        Boolean expectedResponseBody = true;

        when(service.register(dto)).thenReturn(expectedResponseBody);
        ResponseEntity<Boolean> actual = authenticationController.registerDentist(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertEquals(Boolean.TRUE, actual.getBody());
    }
    @Test
    void registerDentistShouldReturnOkStatusWithoutBody() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");

        ResponseEntity<Boolean> actual = authenticationController.registerDentist(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertTrue(actual.hasBody());
        }
    @Test
    void authenticateShouldReturnOkStatusAndExpectedValue() {
        AuthenticationRequest request = new AuthenticationRequest(
                                                            "email@e.com",
                                                         "password",
                                                             "CUSTOMER",
                                                  "0"
                                                                 );
        String token = "token";
        AuthenticationResponse expected = AuthenticationResponse.builder()
                .token(token)
                .id(USER_ID)
                .build();

        when(service.authenticate(request)).thenReturn(expected);

        ResponseEntity<AuthenticationResponse> actualResponse = authenticationController.authenticate(request);

        assertEquals(expected,actualResponse.getBody());
    }
    @Test
    void authenticateShouldReturnOkStatusWithoutBody() {
        AuthenticationRequest request = new AuthenticationRequest(
                "emaile.com",
                "password",
                "CUSTOMER",
                "0"
        );
        ResponseEntity<AuthenticationResponse> actual = authenticationController.authenticate(request);

        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertFalse(actual.hasBody());
    }
}
