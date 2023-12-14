package org.rmnorbert.dentocrates.dto.leave;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record LeaveRegisterDTO (@Min(1) long clinicId,
                                LocalDateTime startOfTheLeave,
                                LocalDateTime endOfTheLeave
                               ){
}
