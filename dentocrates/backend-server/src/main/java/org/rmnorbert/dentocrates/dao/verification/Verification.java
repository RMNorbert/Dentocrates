package org.rmnorbert.dentocrates.dao.verification;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rmnorbert.dentocrates.data.authentication.Role;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private final String email;

    @NotBlank
    private String verificationCode;

    private LocalDateTime registrationTime;

    @Enumerated(EnumType.STRING)
    private Role role;
    @PrePersist
    public void prePersist() {
        if (registrationTime == null) {
            registrationTime = LocalDateTime.now();
        }
    }
    public static Verification of(String email, Role role, String code) {
        return Verification.builder()
                .verificationCode(code)
                .email(email)
                .role(role)
                .build();
    }
}
