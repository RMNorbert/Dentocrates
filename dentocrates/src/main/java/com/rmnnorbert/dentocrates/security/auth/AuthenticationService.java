package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import com.rmnnorbert.dentocrates.dto.client.authentication.VerificationRequestDTO;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.repository.ClientRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.service.JwtService;
import com.rmnnorbert.dentocrates.service.client.DentistService;
import com.rmnnorbert.dentocrates.service.client.VerificationService;
import com.rmnnorbert.dentocrates.utils.DtoMapper;
import com.rmnnorbert.dentocrates.service.client.OAuth2HelperService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
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
    private String state;
    private final ClientRepository clientRepository;
    private final DentistService dentistService;
    private final CustomerRepository customerRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final OAuth2HelperService oAuth2Helper;
    private final JwtService jwtService;
    private final VerificationService verificationService;
    @Autowired
    public AuthenticationService(ClientRepository clientRepository, DentistService dentistService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, VerificationService verificationService, ClientRegistrationRepository clientRegistrationRepository, OAuth2HelperService oAuth2Helper) {
        this.clientRepository = clientRepository;
        this.dentistService = dentistService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2Helper = oAuth2Helper;
        loginSuccessCounter = Metrics.counter("counter.login.success");
        loginFailureCounter = Metrics.counter("counter.login.failure");
    }

    public AuthenticationResponse register(CustomerRegisterDTO request) {
        if(getClient(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Customer customer = DtoMapper.toEntity(request,password);

            customerRepository.save(customer);
            verificationService.sendVerification(request.email(), "CUSTOMER","registration", false);

            String jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidCredentialException();
        }
    }
    public AuthenticationResponse register(DentistRegisterDTO request) {
        if(getClient(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Dentist dentist = DtoMapper.toEntity(request,password);

            dentistService.saveDentist(dentist);
            verificationService.sendVerification(request.email(), "DENTIST","registration", false);

            String jwtToken = jwtService.generateToken(dentist);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidCredentialException();
        }
    }
    public AuthenticationRequest registerWithOauth(String state, String code) {
        String[] userCredentials = oAuth2Helper.getOauthCredentials(this.state , state, code);
        String email = userCredentials[0];
        String password = userCredentials[1];
        String firstName = userCredentials[2];
        String lastName = userCredentials[3];

        String authenticationCode;
        try {
            Optional<Client> optionalClient = getClient(email);
            if (optionalClient.isEmpty()) {
                CustomerRegisterDTO customerRegisterDTO = new CustomerRegisterDTO(email, password, firstName, lastName);
                register(customerRegisterDTO);
            }

            authenticationCode = verificationService.sendAuthenticationCode(email,"CUSTOMER");
            return new AuthenticationRequest(email, password,"CUSTOMER", authenticationCode);
        }catch (Exception e) {
            authenticationCode = verificationService.sendAuthenticationCode(email,"CUSTOMER");
            return new AuthenticationRequest(email, password,"CUSTOMER", authenticationCode);
        }
    }

    public boolean sendAuthenticationCode(VerificationRequestDTO dto) {
        boolean isEmailExist = clientRepository.existsByEmail(dto.email());
        if (isEmailExist) {
            String role = clientRepository.findRoleByEmail(dto.email()).get().toString();
            verificationService.sendAuthenticationCode(dto.email(), role);
            return true;
        }
        return false;
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<Client> optionalClient = getClient(request.email());

        if (optionalClient.isPresent()) {
            boolean isAuthenticationCodeValid = verificationService.validate(request.authenticationCode());

            if(isAuthenticationCodeValid) {
                Client client = optionalClient.get();

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );
                loginSuccessCounter.increment();
                HashMap<String, Object> additionalClaims = new HashMap<>();
                additionalClaims.put("role", client.getRole());
                String jwtToken = jwtService.generateToken(additionalClaims, client);
                verificationService.deleteVerification(request.authenticationCode());

                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .id(client.getId())
                        .build();
            }
        }
        loginFailureCounter.increment();
        throw new InvalidCredentialException();
    }
    public String getAuthorizationUrl() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        if (clientRegistration != null) {
            String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
            String clientId = clientRegistration.getClientId();
            String redirectUri ="http://localhost:3000/login/oauth2/code/";
            String scope = String.join(" ", clientRegistration.getScopes());
            String state = generateState();

            String authorizationUrl = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code&state=%s",
                    authorizationUri, clientId, redirectUri, scope, state);
            return authorizationUrl;
        }
        return null;
    }
    private Optional<Client> getClient(String email) {
        return clientRepository.getClientByEmail(email);
    }
    private String generateState() {
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        this.state = state;
        return state;
    }
}
