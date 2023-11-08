package com.rmnnorbert.dentocrates.repository.clinic.location;

import com.rmnnorbert.dentocrates.dao.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> getByZipCode(int zipCode);
}
