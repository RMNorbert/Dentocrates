package org.rmnorbert.dentocrates.dao.client;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rmnorbert.dentocrates.data.authentication.Role;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerRegisterDTO;

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
    public static Customer toEntity(CustomerRegisterDTO request, String password){
        return Customer.builder()
                .email(request.email())
                .password(password)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.CUSTOMER)
                .build();
    }
}
