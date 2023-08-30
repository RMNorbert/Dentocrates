package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository<T extends Client> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);
    @Query("DELETE FROM Client c WHERE FUNCTION('HOUR_ADD', c.registrationTime, 12) < NOW() AND c.verified = false")
    int deleteUnverifiedClients();
}
