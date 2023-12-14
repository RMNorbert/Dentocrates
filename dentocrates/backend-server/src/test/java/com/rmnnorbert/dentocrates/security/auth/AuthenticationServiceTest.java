package com.rmnnorbert.dentocrates.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rmnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import org.rmnorbert.dentocrates.dao.client.Client;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import org.rmnorbert.dentocrates.repository.client.ClientRepository;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.security.auth.AuthenticationService;
import org.rmnorbert.dentocrates.security.auth.JwtService;
import org.rmnorbert.dentocrates.security.auth.loginHistory.LoginHistoryService;
import org.rmnorbert.dentocrates.service.client.communicationServices.VerificationService;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
import org.rmnorbert.dentocrates.service.client.oauth2.OAuth2HelperService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    private static long USER_ID = 1;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DentistService dentistService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private VerificationService verificationService;
    @Mock
    private OAuth2HelperService oAuth2Helper;
    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;
    @Mock
    private LoginHistoryService loginHistoryService;
    private AuthenticationService authenticationService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                clientRepository,
                dentistService,
                customerRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                verificationService,
                clientRegistrationRepository,
                oAuth2Helper,
                loginHistoryService);
    }

    @Test
    void registerCustomerShouldReturnExpectedBooleanResponse() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email", "password", "name", "last");

        Boolean actual = authenticationService.register(dto);

        assertTrue(actual);
    }
    @Test
    void registerAlreadyRegisteredCustomerShouldReturnInvalidCredentialException() {
        CustomerRegisterDTO dto = new CustomerRegisterDTO("email", "password", "name", "last");
        Customer customer = new Customer(USER_ID,dto.email(), dto.password(), dto.firstName(), dto.lastName(),Role.CUSTOMER);

        when(clientRepository.getClientByEmail(dto.email())).thenReturn(Optional.of(customer));

        assertThrows(InvalidCredentialException.class, () -> authenticationService.register(dto));
    }
    @Test
    void registerDentistShouldReturnExpectedBooleanResponse() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email", "password", "name", "last","opLicence");
        Boolean expected = true;

        Boolean actual = authenticationService.register(dto);

        assertEquals(expected, actual);
    }
    @Test
    void registerAlreadyRegisteredDentistShouldReturnInvalidCredentialException() {
        DentistRegisterDTO dto = new DentistRegisterDTO("email", "password", "name", "last","opLicence");
        Dentist dentist = new Dentist(USER_ID,dto.email(), dto.password(), dto.firstName(), dto.lastName(),dto.operatingLicenceNo(),Role.DENTIST);

        when(clientRepository.getClientByEmail(dto.email())).thenReturn(Optional.of(dentist));

        assertThrows(InvalidCredentialException.class, () -> authenticationService.register(dto));
    }
    @Test
    void authenticateShouldReturnExpectedAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest("email", "password","CUSTOMER","0");
        Optional<Client> optionalClient = Optional.of(new Client(USER_ID,"email","password","first","last",Role.CUSTOMER,false, LocalDateTime.now()));
        HashMap<String, Object> additionalClaims = new HashMap<>();
        long expectedId = 1L;
        String jwtToken = "Token";

        when(clientRepository.getClientByEmail(request.email())).thenReturn(optionalClient);
        when(jwtService.generateToken(additionalClaims, optionalClient.get())).thenReturn(jwtToken);
        when(passwordEncoder.matches(request.password(),request.password())).thenReturn(true);
        when(verificationService.validate(request.authenticationCode(),request.email())).thenReturn(true);

        AuthenticationResponse expected = new AuthenticationResponse(jwtToken,expectedId);
        AuthenticationResponse actual = authenticationService.authenticate(request);

        assertAuthenticationResponse(expected,actual);
        assertEquals(expected.id(),actual.id());
    }
    @Test
    void authenticateWithWrongShouldReturnInvalidCredentialException() {
        AuthenticationRequest request = new AuthenticationRequest("email", "password","CUSTOMER","0");
        String password = passwordEncoder.encode(request.password());
        when(clientRepository.getClientByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () -> authenticationService.authenticate(request));
    }

    private boolean assertAuthenticationResponse(AuthenticationResponse expected, AuthenticationResponse actual) {
        return expected.token().equals(actual.token());
    }
}
