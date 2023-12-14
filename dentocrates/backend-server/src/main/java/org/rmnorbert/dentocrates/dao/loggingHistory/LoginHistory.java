package org.rmnorbert.dentocrates.dao.loggingHistory;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    LocalDateTime time;
    @Email
    private final String email;
    @NonNull
    String ip_address;
    @NonNull
    String user_agent;

    public static LoginHistory of(String email, String ipAddress, String userAgent) {
        return LoginHistory.builder()
                .email(email)
                .time(LocalDateTime.now())
                .ip_address(ipAddress)
                .user_agent(userAgent)
                .build();
    }
}
