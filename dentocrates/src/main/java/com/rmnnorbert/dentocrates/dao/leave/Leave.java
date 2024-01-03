package com.rmnnorbert.dentocrates.dao.leave;

import com.rmnnorbert.dentocrates.dao.clinic.Clinic;
import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NonNull
    private final Clinic clinic;

    @NonNull
    private final LocalDateTime startOfTheLeave;

    @NonNull
    private final LocalDateTime endOfTheLeave;

    public static Leave of(LeaveRegisterDTO dto , Clinic clinicToRegister) {
        return Leave.builder()
                .clinic(clinicToRegister)
                .startOfTheLeave(dto.startOfTheLeave())
                .endOfTheLeave(dto.endOfTheLeave())
                .build();
    }
}
