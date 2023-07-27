package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.repository.ClientRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.security.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    private AuthenticationService authenticationService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                clientRepository,
                dentistRepository,
                customerRepository,
                passwordEncoder,
                jwtService,
                authenticationManager
        );
    }

    @Test
    void registerCustomerShouldReturnExpectedAuthenticationResponse() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email", "password", "name", "last");
        Customer customer = new Customer(1L,dto.email(), dto.password(), dto.firstName(), dto.lastName(),Role.CUSTOMER);
        String jwtToken = "token";

        when(jwtService.generateToken(customer)).thenReturn(jwtToken);

        AuthenticationResponse expected = AuthenticationResponse.builder()
                                                                .token(jwtToken)
                                                                .build();
        AuthenticationResponse actual = authenticationService.register(dto);

        assertAuthenticationResponse(expected, actual);
    }
    @Test
    void registerAlreadyRegisteredCustomerShouldReturnInvalidCredentialException() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email", "password", "name", "last");
        Customer customer = new Customer(1L,dto.email(), dto.password(), dto.firstName(), dto.lastName(),Role.CUSTOMER);

        when(clientRepository.findByEmail(dto.email())).thenReturn(Optional.of(customer));

        assertThrows(InvalidCredentialException.class, () -> authenticationService.register(dto));
        verify(clientRepository,times(1)).findByEmail(dto.email());
    }
    @Test
    void registerDentistShouldReturnExpectedAuthenticationResponse() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email", "password", "name", "last","opLicence");
        Dentist dentist = new Dentist(1L,dto.email(), dto.password(), dto.firstName(), dto.lastName(),dto.operatingLicenceNo(),Role.DENTIST);
        String jwtToken = "token";

        when(jwtService.generateToken(dentist)).thenReturn(jwtToken);

        AuthenticationResponse expected = AuthenticationResponse.builder()
                                                                .token(jwtToken)
                                                                .build();
        AuthenticationResponse actual = authenticationService.register(dto);

        assertAuthenticationResponse(expected, actual);
    }
    @Test
    void registerAlreadyRegisteredDentistShouldReturnInvalidCredentialException() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email", "password", "name", "last","opLicence");
        Dentist dentist = new Dentist(1L,dto.email(), dto.password(), dto.firstName(), dto.lastName(),dto.operatingLicenceNo(),Role.DENTIST);

        when(clientRepository.findByEmail(dto.email())).thenReturn(Optional.of(dentist));

        assertThrows(InvalidCredentialException.class, () -> authenticationService.register(dto));
        verify(clientRepository,times(1)).findByEmail(dto.email());
    }
    @Test
    void authenticateShouldReturnExpectedAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest("email", "password");
        Optional<Client> optionalClient = Optional.of(new Client(1L,"email","password","first","last",Role.CUSTOMER));
        HashMap<String, Object> additionalClaims = new HashMap<>();
        long expectedId = 1L;
        String jwtToken = "Token";

        when(clientRepository.findByEmail(request.email())).thenReturn(optionalClient);
        when(jwtService.generateToken(additionalClaims, optionalClient.get())).thenReturn(jwtToken);

        AuthenticationResponse expected = new AuthenticationResponse(jwtToken,expectedId);
        AuthenticationResponse actual = authenticationService.authenticate(request);

        assertAuthenticationResponse(expected,actual);
        assertEquals(expected.id(),actual.id());
        verify(clientRepository,times(1)).findByEmail(request.email());
    }
    @Test
    void authenticateWithWrongShouldReturnInvalidCredentialException() {
        AuthenticationRequest request = new AuthenticationRequest("email", "password");

        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () -> authenticationService.authenticate(request));
        verify(clientRepository,times(1)).findByEmail(request.email());
    }

    private boolean assertAuthenticationResponse(AuthenticationResponse expected, AuthenticationResponse actual) {
        return expected.token().equals(actual.token());
    }
}
