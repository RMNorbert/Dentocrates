package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.client.authentication.AuthenticationController;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
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
        String jwtToken = "token";

        AuthenticationResponse expectedResponseBody = AuthenticationResponse.builder()
                .token(jwtToken)
                .id(0)
                .build();

        when(service.register(dto)).thenReturn(expectedResponseBody);

        ResponseEntity<AuthenticationResponse> actual = authenticationController.register(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponseBody.id(), actual.getBody().id());
        assertEquals(expectedResponseBody.token(), actual.getBody().token());

        verify(service,times(1)).register(dto);
    }
    @Test
    void registerCustomerShouldReturnOkStatusWithNoBody() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email@e.com",
                                                       "password",
                                                       "name",
                                                        "last");

        ResponseEntity<AuthenticationResponse> actual = authenticationController.register(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertFalse(actual.hasBody());
    }
    @Test
    void registerDentistShouldReturnOkStatusAndExpectedValue() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");

        String jwtToken = "token";

        AuthenticationResponse expectedResponseBody = AuthenticationResponse.builder()
                .token(jwtToken)
                .id(0)
                .build();

        when(service.register(dto)).thenReturn(expectedResponseBody);
        ResponseEntity<AuthenticationResponse> actual = authenticationController.registerDentist(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponseBody.id(), actual.getBody().id());
        assertEquals(expectedResponseBody.token(), actual.getBody().token());

        verify(service,times(1)).register(dto);
    }
    @Test
    void registerDentistShouldReturnOkStatusWithoutBody() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email@e.com",
                                                     "password",
                                                     "name",
                                                     "last",
                                             "opLicence");

        ResponseEntity<AuthenticationResponse> actual = authenticationController.registerDentist(dto);
        assertTrue(actual.getStatusCode().is2xxSuccessful());
        assertFalse(actual.hasBody());
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
        long id = 0;
        AuthenticationResponse expected = AuthenticationResponse.builder()
                .token(token)
                .id(id)
                .build();

        when(service.authenticate(request)).thenReturn(expected);

        ResponseEntity<AuthenticationResponse> actualResponse = authenticationController.authenticate(request);

        assertEquals(expected,actualResponse.getBody());
        verify(service,times(1)).authenticate(request);
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
