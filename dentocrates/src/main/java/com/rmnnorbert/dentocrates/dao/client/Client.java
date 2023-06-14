package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor()
@MappedSuperclass
public abstract class Client {
    @Column(unique = true)
    @Email
    protected final String email;
    @NotBlank
    @Size(min = 8)
    protected final String password;
    @NotBlank
    protected final String firstName;
    @NotBlank
    protected final String lastName;
    @NotBlank
    @Enumerated(EnumType.STRING)
    protected final Role role;

}
