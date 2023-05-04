package com.rmnnorbert.dentocrates.repository;

import com.rmnnorbert.dentocrates.dao.client.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
