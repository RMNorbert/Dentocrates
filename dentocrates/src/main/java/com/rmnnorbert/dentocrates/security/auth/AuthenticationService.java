package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidLoginException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.repository.ClientRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.repository.DentistRepository;
import com.rmnnorbert.dentocrates.security.config.JwtService;
import com.rmnnorbert.dentocrates.utils.DtoMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public AuthenticationService(ClientRepository clientRepository, DentistRepository dentistRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.dentistRepository = dentistRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        loginSuccessCounter = Metrics.counter("counter.login.success");
        loginFailureCounter = Metrics.counter("counter.login.failure");
    }

    public AuthenticationResponse register(CustomerRegisterDTO request) {
        if(clientRepository.findByEmail(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Customer customer = DtoMapper.toEntity(request,password);
            customerRepository.save(customer);

            String jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidLoginException();
        }
    }
    public AuthenticationResponse register(DentistRegisterDTO request) {
        if(clientRepository.findByEmail(request.email()).isEmpty()){
            String password = passwordEncoder.encode(request.password());
            Dentist dentist = DtoMapper.toEntity(request,password);
            dentistRepository.save(dentist);

            String jwtToken = jwtService.generateToken(dentist);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidLoginException();
        }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
            try {
            Optional<Client> optionalClient = clientRepository.findByEmail(request.email());
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
        }catch (Exception e){
                loginFailureCounter.increment();
                throw new InvalidLoginException();
        }
        throw new InvalidLoginException();
    }
}
