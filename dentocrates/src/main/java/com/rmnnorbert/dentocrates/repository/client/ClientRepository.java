package com.rmnnorbert.dentocrates.repository.client;

import com.rmnnorbert.dentocrates.dao.client.Client;
import com.rmnnorbert.dentocrates.data.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository<T extends Client> extends JpaRepository<T, Long> {
    Optional<T> getClientByEmail(String email);

    @Query("SELECT c.role FROM Client c WHERE c.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM Client c WHERE c.registration_time + interval '1h' * :hours  < CURRENT_TIMESTAMP AND c.verified = false", nativeQuery = true)
    void deleteUnverifiedClients(@Param("hours") int hours);
}
