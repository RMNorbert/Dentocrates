package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(force = true)
@DiscriminatorValue("CUSTOMER")
public class Customer extends Client {
    @Builder
    public Customer(String email, String password, String firstName, String lastName, Role role) {
        super(email, password, firstName, lastName, role);
    }

}
