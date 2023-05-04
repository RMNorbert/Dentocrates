package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);
}
