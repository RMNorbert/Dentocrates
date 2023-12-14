package org.rmnorbert.dentocrates.repository.client;

import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DentistRepository extends ClientRepository<Dentist> {
    Optional<Dentist> getClientByEmail(String email);
}
