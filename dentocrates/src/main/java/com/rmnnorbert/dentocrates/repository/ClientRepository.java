package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository<T extends Client> extends JpaRepository<T, Long> {
    Optional<T> getClientByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT c.role FROM Client c WHERE c.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);

    @Query("DELETE FROM Client c WHERE FUNCTION('HOUR_ADD', c.registrationTime, 12) < NOW() AND c.verified = false")
    int deleteUnverifiedClients();
}
