package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidOAuth2ClientRegistrationException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationRequest;
import com.rmnnorbert.dentocrates.dto.client.authentication.AuthenticationResponse;
import com.rmnnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.dto.client.update.ForgotPasswordDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerificationRequestDTO;
import com.rmnnorbert.dentocrates.dto.client.verification.VerifyDTO;
import com.rmnnorbert.dentocrates.repository.client.ClientRepository;
import com.rmnnorbert.dentocrates.repository.client.CustomerRepository;
import com.rmnnorbert.dentocrates.security.auth.loginHistory.LoginHistoryService;
import com.rmnnorbert.dentocrates.service.client.communicationServices.VerificationService;
import com.rmnnorbert.dentocrates.service.client.dentist.DentistService;
import com.rmnnorbert.dentocrates.service.client.oauth2.OAuth2HelperService;
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
    /** The role string representing the 'CUSTOMER' role. */
    private final static String CUSTOMER_ROLE = "CUSTOMER";
    /** The role string representing the 'DENTIST' role. */
    private final static String DENTIST_ROLE = "DENTIST";
    /** The action string representing the 'registration' action. */
    private final static String REGISTRATION_ACTION = "registration";
    /** Index of the email in the user credentials array. */
    private final static int EMAIL_INDEX = 0;
    /** Index of the password in the user credentials array. */
    private final static int PASSWORD_INDEX = 1;
    /** Index of the first name in the user credentials array. */
    private final static int FIRST_NAME_INDEX = 2;
    /** Index of the last name in the user credentials array. */
    private final static int LAST_NAME_INDEX = 3;
    /** The redirect URI used during OAuth flow retrieved from the environment variables. */
    private final String REDIRECT_URI = System.getenv("REDIRECT_URI");
    /** The state variable used in OAuth flows. */
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

    /**
     * Registers a new customer based on the provided registration information.
     * If the email is not registered yet and the registration successful
     * a verification code sent by the verification service
     *
     * @param request The DTO containing customer registration details.
     * @return True if the registration is successful; false otherwise.
     * @throws InvalidCredentialException if a customer with the provided email already exists.
     */
    public Boolean register(CustomerRegisterDTO request) {
        if(isEmailNotRegistered(request.email())){
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
        if(isEmailNotRegistered(request.email())){
            String password = passwordEncoder.encode(request.password());
            Dentist dentist = Dentist.toEntity(request,password);

            dentistService.saveDentist(dentist);
            verificationService.sendVerification(request.email(), DENTIST_ROLE, REGISTRATION_ACTION, false);

            return true;
        } else{
            throw new InvalidCredentialException();
        }
    }

    /**
     * Registers a user with OAuth2 credentials, generating an authentication code for login.
     *
     * @param state The OAuth2 state parameter.
     * @param code The OAuth2 authorization code.
     * @return An AuthenticationRequest containing user credentials and authentication code.
     */
    public AuthenticationRequest registerWithOauth(String state, String code) {
        // Extract user credentials from OAuth2 authentication
        String[] userCredentials = oAuth2Helper.getOauthCredentials(this.state , state, code);
        String email = userCredentials[EMAIL_INDEX];
        String password = userCredentials[PASSWORD_INDEX];
        String firstName = userCredentials[FIRST_NAME_INDEX];
        String lastName = userCredentials[LAST_NAME_INDEX];
        String authenticationCode;
        try {
            registerIfNotExists(email, password, firstName, lastName);

            authenticationCode = generateAuthenticationCode(email,CUSTOMER_ROLE);
            return new AuthenticationRequest(email, password,CUSTOMER_ROLE, authenticationCode);
        }catch (Exception e) {
            authenticationCode = generateAuthenticationCode(email,CUSTOMER_ROLE);
            return new AuthenticationRequest(email, password,CUSTOMER_ROLE, authenticationCode);
        }
    }

    /**
     * Sends an authentication code for login after verifying the provided email and password.
     *
     * @param dto The VerificationRequestDTO containing the email and password for authentication.
     * @return True if the email and password are valid, and an authentication code is generated; false otherwise.
     */
    public boolean sendAuthenticationCode(VerificationRequestDTO dto) {
        Client client = getClientByEmail(dto.email()).orElseThrow(InvalidCredentialException::new);
        String storedPassword = client.getPassword();
        boolean isRequestValid = passwordEncoder.matches(dto.password(), storedPassword);
        if (isRequestValid) {
            String role = clientRepository.findRoleByEmail(dto.email()).get().toString();
            generateAuthenticationCode(dto.email(), role);
            return true;
        }
        loginHistoryService.unSuccessfulLogin(client.getEmail());
        return false;
    }

    /**
     * Authenticates a user based on the provided credentials and authentication code.
     *
     * @param request The AuthenticationRequest containing the user's email, password, and authentication code.
     * @return An AuthenticationResponse if the credentials and authentication code are valid.
     * @throws InvalidCredentialException If the provided credentials or authentication code are not valid.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Client client = getClientByEmail(request.email()).orElseThrow(InvalidCredentialException::new);
        String storedPassword = client.getPassword();
        boolean isPasswordValid = passwordEncoder.matches(request.password(), storedPassword);

        if (isPasswordValid) {
            // If the password is valid, attempt to authenticate with the authentication manager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            boolean isAuthenticationCodeValid = verificationService.validate(request.authenticationCode(),
                                                                             request.email());
            if (isAuthenticationCodeValid) {
                return generateAuthenticationResponse(client, request);
            }
        }
        loginHistoryService.unSuccessfulLogin(client.getEmail());
        throw new InvalidCredentialException();
    }

    /**
     * Generates the authorization URL for OAuth2 authentication with a specific identity provider.
     *
     * @return The authorization URL for initiating OAuth2 authentication.
     * @throws InvalidOAuth2ClientRegistrationException If the client registration for the specified identity provider
     * is not found.
     */
    public String getAuthorizationUrl() {
        // Retrieve the client registration for the Google identity provider
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");

        if (clientRegistration != null) {
            // Extract necessary details from the client registration
            String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
            String clientId = clientRegistration.getClientId();
            String scope = String.join(" ", clientRegistration.getScopes());
            String state = generateState();

            // Construct the authorization URL
            String authorizationUrl = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code&state=%s",
                    authorizationUri, clientId, REDIRECT_URI, scope, state);
            return authorizationUrl;
        }
        throw new InvalidOAuth2ClientRegistrationException();
    }

    /**
     * Initiates the process for resetting the password by generating and sending an authentication code.
     *
     * @param dto The data transfer object containing the requester's email for password reset.
     * @return True if the password reset process is successfully initiated; otherwise, false.
     */
    public Boolean requestReset(ForgotPasswordDTO dto) {
        Client client = getClientByEmail(dto.requesterEmail()).orElseThrow(InvalidCredentialException::new);
        generateAuthenticationCode(dto.requesterEmail(),client.getRole().toString());
        return true;
    }

    private Optional<Client> getClientByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }
    private boolean isEmailNotRegistered (String email) {
        return clientRepository.getClientByEmail(email).isEmpty();
    }
    private void registerIfNotExists(String email, String password, String firstName, String lastName) {
        if (isEmailNotRegistered(email)) {
            CustomerRegisterDTO customerRegisterDTO = new CustomerRegisterDTO(email, password, firstName, lastName);
            register(customerRegisterDTO);
        }
    }

    /**
     * Generates an authentication response containing a JWT token for a successful login,
     * updates login history, and deletes the used verification entry.
     *
     * @param client The authenticated client.
     * @param request The authentication request containing user details.
     * @return AuthenticationResponse containing JWT token and client ID.
     */
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

    /**
     * Generates an authentication code for the specified user email and role.
     * The authentication code is sent to the user's email for authentication purposes.
     *
     * @param email The email address of the user for whom the authentication code is generated.
     * @param role  The role of the user (e.g., "CUSTOMER", "DENTIST") for authentication purposes.
     * @return The generated authentication code.
     */
    private String generateAuthenticationCode(String email, String role) {
        return verificationService.sendAuthenticationCode(email, role);
    }

    /**
     * Generates a state string to be used in OAuth2 authentication flow.
     * The state is a unique string used to prevent cross-site request forgery (CSRF) attacks.
     *
     * @return The generated state string.
     */
    private String generateState() {
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        this.state = state;
        return state;
    }
}
