package com.rmnnorbert.dentocrates.controller.dto.clinic.leave;

public record LeaveDeleteDTO(long dentistId,
                            long leaveId,
                            long clinicId
                            ) {
}
