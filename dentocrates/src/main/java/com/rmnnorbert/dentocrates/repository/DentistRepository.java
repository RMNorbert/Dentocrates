package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Dentist;

import java.util.Optional;

public interface DentistRepository extends ClientRepository<Dentist> {
    Optional<Dentist> findByEmail(String email);
}
