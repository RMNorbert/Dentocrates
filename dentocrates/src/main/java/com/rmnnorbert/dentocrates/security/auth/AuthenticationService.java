package com.rmnnorbert.dentocrates.security.auth;

import com.rmnnorbert.dentocrates.controller.dto.client.customer.CustomerRegisterDTO;
import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.customExceptions.InvalidLoginException;
import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.dao.client.Customer;
import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.repository.ClientRepository;
import com.rmnnorbert.dentocrates.repository.CustomerRepository;
import com.rmnnorbert.dentocrates.security.config.JwtService;
import com.rmnnorbert.dentocrates.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

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
            clientRepository.save(dentist);

            String jwtToken = jwtService.generateToken(dentist);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else{
            throw new InvalidLoginException();
        }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Throwable {
            try {
            Optional<Client> optionalClient = clientRepository.findByEmail(request.getEmail());
            if(optionalClient.isPresent()) {
                Client client = optionalClient.get();
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

                HashMap<String, Object> additionalClaims = new HashMap<>();
                additionalClaims.put("role", client.getRole());
                String jwtToken = jwtService.generateToken(additionalClaims, client);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .id(client.id)
                        .build();
            }
        }catch (Exception e){
            throw new InvalidLoginException();
        }
        throw new InvalidLoginException();
    }
}
