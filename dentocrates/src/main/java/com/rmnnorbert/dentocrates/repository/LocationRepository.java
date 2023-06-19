package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.clinic.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> getByZipCode(int zipCode);
}
