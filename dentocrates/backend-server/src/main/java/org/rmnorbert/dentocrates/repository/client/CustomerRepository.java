package org.rmnorbert.dentocrates.repository.client;

import org.rmnorbert.dentocrates.dao.client.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends ClientRepository<Customer> {
    Optional<Customer> getClientByEmail(String email);
}
