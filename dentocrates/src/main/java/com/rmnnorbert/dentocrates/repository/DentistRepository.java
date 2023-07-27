package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Dentist;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DentistRepository extends ClientRepository<Dentist> {
    Optional<Dentist> findByEmail(String email);
}
