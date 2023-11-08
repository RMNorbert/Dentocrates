package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.authentication.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;


@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type", discriminatorType = DiscriminatorType.STRING)
@Entity
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true)

    @Email
    protected final String email;

    @Size(min = 8)
    @NotBlank
    protected String password;

    @NotBlank
    protected final String firstName;

    @NotBlank
    protected final String lastName;

    @Enumerated(EnumType.STRING)
    protected final Role role;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean verified;

    protected LocalDateTime registrationTime;
    public Client( String email, String password, String firstName, String lastName, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.verified = false;
        this.registrationTime = LocalDateTime.now();
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
