package org.rmnorbert.dentocrates.dto.leave;

import jakarta.validation.constraints.Min;

public record LeaveDeleteDTO(@Min(1) long dentistId,
                             @Min(1) long leaveId,
                             @Min(1) long clinicId
                            ) {
}
