package org.rmnorbert.dentocrates.security.auth;


import org.rmnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import org.rmnorbert.dentocrates.custom.exceptions.InvalidOAuth2ClientRegistrationException;
import org.rmnorbert.dentocrates.dao.client.Client;
import org.rmnorbert.dentocrates.dao.client.Customer;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import org.rmnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import org.rmnorbert.dentocrates.dto.client.update.ForgotPasswordDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerificationRequestDTO;
import org.rmnorbert.dentocrates.dto.client.verification.VerifyDTO;
import org.rmnorbert.dentocrates.repository.client.ClientRepository;
import org.rmnorbert.dentocrates.repository.client.CustomerRepository;
import org.rmnorbert.dentocrates.security.auth.loginHistory.LoginHistoryService;
import org.rmnorbert.dentocrates.service.client.communicationServices.VerificationService;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
import org.rmnorbert.dentocrates.service.client.oauth2.OAuth2HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final static String CUSTOMER_ROLE = "CUSTOMER";
    private final static String DENTIST_ROLE = "DENTIST";
    private final static String REGISTRATION_ACTION = "registration";
    private final static int EMAIL_INDEX = 0;
    private final static int PASSWORD_INDEX = 1;
    private final static int FIRST_NAME_INDEX = 2;
    private final static int LAST_NAME_INDEX = 3;
    private final String REDIRECT_URI = System.getenv("REDIRECT_URI");
    private String state;
    private final ClientRepository clientRepository;
    private final DentistService dentistService;
    private final CustomerRepository customerRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LoginHistoryService loginHistoryService;
    private final OAuth2HelperService oAuth2Helper;
    private final JwtService jwtService;
    private final VerificationService verificationService;
    @Autowired
    public AuthenticationService(ClientRepository clientRepository, DentistService dentistService,
                                 CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager,
                                 VerificationService verificationService,
                                 ClientRegistrationRepository clientRegistrationRepository,
                                 OAuth2HelperService oAuth2Helper, LoginHistoryService loginHistoryService) {

        this.clientRepository = clientRepository;
        this.dentistService = dentistService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2Helper = oAuth2Helper;
        this.loginHistoryService = loginHistoryService;
    }

    public Boolean register(CustomerRegisterDTO request) {
        if(getClientByEmail(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Customer customer = Customer.toEntity(request,password);

            customerRepository.save(customer);
            verificationService.sendVerification(request.email(), CUSTOMER_ROLE, REGISTRATION_ACTION, false);

            return true;
        } else{
            throw new InvalidCredentialException();
        }
    }
    public Boolean register(DentistRegisterDTO request) {
        if(getClientByEmail(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Dentist dentist = Dentist.toEntity(request,password);

            dentistService.saveDentist(dentist);
            verificationService.sendVerification(request.email(), DENTIST_ROLE, REGISTRATION_ACTION, false);

            return true;
        } else{
            throw new InvalidCredentialException();
        }
    }
    public AuthenticationRequest registerWithOauth(String state, String code) {
        String[] userCredentials = oAuth2Helper.getOauthCredentials(this.state , state, code);
        String email = userCredentials[EMAIL_INDEX];
        String password = userCredentials[PASSWORD_INDEX];
        String firstName = userCredentials[FIRST_NAME_INDEX];
        String lastName = userCredentials[LAST_NAME_INDEX];
        String authenticationCode;
        try {
            Optional<Client> optionalClient = getClientByEmail(email);
            if (optionalClient.isEmpty()) {
                CustomerRegisterDTO customerRegisterDTO = new CustomerRegisterDTO(email, password, firstName, lastName);
                register(customerRegisterDTO);
            }
            authenticationCode = verificationService.sendAuthenticationCode(email,CUSTOMER_ROLE);
            return new AuthenticationRequest(email, password,CUSTOMER_ROLE, authenticationCode);
        }catch (Exception e) {
            authenticationCode = verificationService.sendAuthenticationCode(email,CUSTOMER_ROLE);
            return new AuthenticationRequest(email, password,CUSTOMER_ROLE, authenticationCode);
        }
    }

    public boolean sendAuthenticationCode(VerificationRequestDTO dto) {
        Optional<Client> optionalClient = clientRepository.getClientByEmail(dto.email());
        if(optionalClient.isPresent()) {
            String storedPassword = optionalClient.get().getPassword();
            boolean isRequestValid = passwordEncoder.matches(dto.password(), storedPassword);
            if (isRequestValid) {
                String role = clientRepository.findRoleByEmail(dto.email()).get().toString();
                verificationService.sendAuthenticationCode(dto.email(), role);
                return true;
            }
            loginHistoryService.unSuccessfulLogin(optionalClient.get().getEmail());
        }
        throw new InvalidCredentialException();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<Client> optionalClient = getClientByEmail(request.email());
        if(optionalClient.isPresent()) {
            Client client = optionalClient.get();
            String storedPassword = client.getPassword();
            boolean isPasswordValid = passwordEncoder.matches(request.password(), storedPassword);
            if (isPasswordValid) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );
                boolean isAuthenticationCodeValid = verificationService.validate(request.authenticationCode(),request.email());
                if (isAuthenticationCodeValid) {
                    return generateAuthenticationResponse(client, request);
                }
            }
            loginHistoryService.unSuccessfulLogin(optionalClient.get().getEmail());
        }
        throw new InvalidCredentialException();
    }
    public String getAuthorizationUrl() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        if (clientRegistration != null) {
            String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
            String clientId = clientRegistration.getClientId();
            String scope = String.join(" ", clientRegistration.getScopes());
            String state = generateState();

            String authorizationUrl = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code&state=%s",
                    authorizationUri, clientId, REDIRECT_URI, scope, state);
            return authorizationUrl;
        }
        throw new InvalidOAuth2ClientRegistrationException();
    }
    public Boolean requestReset(ForgotPasswordDTO dto) {
        Client client = getClientByEmail(dto.requesterEmail()).orElseThrow(InvalidCredentialException::new);
        verificationService.sendAuthenticationCode(dto.requesterEmail(),client.getRole().toString());
        return true;
    }
    private Optional<Client> getClientByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }
    private AuthenticationResponse generateAuthenticationResponse(Client client, AuthenticationRequest request) {
        loginHistoryService.successfulLogin(client.getEmail());
        HashMap<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("role", client.getRole());
        String jwtToken = jwtService.generateToken(additionalClaims, client);
        VerifyDTO dto = new VerifyDTO(request.authenticationCode());
        verificationService.deleteVerification(dto);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(client.getId())
                .build();
    }
    private String generateState() {
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        this.state = state;
        return state;
    }


}
