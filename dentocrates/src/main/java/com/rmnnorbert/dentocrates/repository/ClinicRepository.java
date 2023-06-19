package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic,Long> {
    Optional<Clinic> findByName(String name);
}
