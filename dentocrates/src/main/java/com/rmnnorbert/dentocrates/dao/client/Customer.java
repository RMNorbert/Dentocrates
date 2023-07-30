package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(force = true)
@DiscriminatorValue("CUSTOMER")
@Entity
public class Customer extends Client {
    @Builder
    public Customer(Long id,String email, String password, String firstName, String lastName, Role role) {
        super(email, password, firstName, lastName, role);
        this.id = id;
    }
}
