package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Customer;

import java.util.Optional;

public interface CustomerRepository extends ClientRepository<Customer> {
    Optional<Customer> findByEmail(String email);

}
