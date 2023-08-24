package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.update.ResetDto;
import com.rmnnorbert.dentocrates.controller.dto.client.update.VerifyDto;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.custom.exceptions.NotFoundException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.dao.verification.Verification;
import com.rmnnorbert.dentocrates.data.Role;
import com.rmnnorbert.dentocrates.repository.ClientRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.security.config.JwtService;
import com.rmnnorbert.dentocrates.service.VerificationService;
import com.rmnnorbert.dentocrates.utils.DtoMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final DentistRepository dentistRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final VerificationService verificationService;
    @Autowired
    public AuthenticationService(ClientRepository clientRepository, DentistRepository dentistRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, VerificationService verificationService) {
        this.clientRepository = clientRepository;
        this.dentistRepository = dentistRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
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

            dentistRepository.save(dentist);
            verificationService.sendVerification(request.email(), "DENTIST","registration", false);

            String jwtToken = jwtService.generateToken(dentist);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidCredentialException();
        }
    }
    public ResponseEntity<String> resetPassword(ResetDto dto) {
        Verification verification = getVerification(dto.verificationCode());

        if(verification.getEmail().equals(dto.email())) {
            updateClientPassword(verification, dto.password());
            return ResponseEntity.ok().body("Password changed successfully.");
        } else {
            throw new InvalidCredentialException();
        }
    }

    public ResponseEntity<String> verifyClient(VerifyDto dto) {
        Verification verification = getVerification(dto.verificationCode());

        if(verification.getRole().equals(Role.CUSTOMER)) {
            Customer customer = getCustomer(verification.getEmail());
            customer.setVerified(true);
            customerRepository.save(customer);
        }
        else if (verification.getRole().equals(Role.DENTIST)) {
            Dentist dentist = getDentist(verification.getEmail());
            dentist.setVerified(true);
            dentistRepository.save(dentist);
        }
        return ResponseEntity.ok().body("Verification successful.");
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<Client> optionalClient = getClient(request.email());
            if(optionalClient.isPresent()) {
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
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .id(client.getId())
                        .build();
            }
            else {
                loginFailureCounter.increment();
                throw new InvalidCredentialException();
            }
    }
    private Optional<Client> getClient(String email) {
        return clientRepository.findByEmail(email);
    }
    private Verification getVerification(String verificationCode) {
        return verificationService.getVerification(verificationCode)
                .orElseThrow(() -> new NotFoundException("Verification"));
    }
    private Customer getCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
    private Dentist getDentist(String email) {
        return dentistRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dentist"));
    }
    private void updateClientPassword(Verification verification, String password ) {
        String newPassword = passwordEncoder.encode(password);

        if (verification.getRole().equals(Role.CUSTOMER)) {
            Customer customer = getCustomer(verification.getEmail());
            customer.setPassword(newPassword);
            customerRepository.save(customer);
        }

        else if (verification.getRole().equals(Role.DENTIST)) {
            Dentist dentist = getDentist(verification.getEmail());
            dentist.setPassword(newPassword);
            dentistRepository.save(dentist);
        }
    }
}
