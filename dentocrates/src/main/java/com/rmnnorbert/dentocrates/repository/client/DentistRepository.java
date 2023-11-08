package com.rmnnorbert.dentocrates.repository.client;

import com.rmnnorbert.dentocrates.dao.client.Dentist;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DentistRepository extends ClientRepository<Dentist> {
    Optional<Dentist> getClientByEmail(String email);
}
