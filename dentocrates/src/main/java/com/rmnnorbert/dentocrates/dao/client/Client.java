package com.rmnnorbert.dentocrates.dao.client;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;


@Getter
@Setter
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor()
@MappedSuperclass
public abstract class Client {
    @Column(unique = true)
    protected final String email;
    protected final String password;
    protected final String firstName;
    protected final String lastName;
    protected final int authorizationCategory;

}
