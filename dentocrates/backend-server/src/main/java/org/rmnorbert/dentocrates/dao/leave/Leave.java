package org.rmnorbert.dentocrates.dao.leave;


import jakarta.persistence.*;
import lombok.*;
import org.rmnorbert.dentocrates.dao.clinic.Clinic;
import org.rmnorbert.dentocrates.dto.leave.LeaveRegisterDTO;

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
