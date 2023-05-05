package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist,Long> {
    Optional<Dentist> findByEmail(String email);
}
