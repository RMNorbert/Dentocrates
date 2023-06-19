package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository<T extends Client> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);

}
