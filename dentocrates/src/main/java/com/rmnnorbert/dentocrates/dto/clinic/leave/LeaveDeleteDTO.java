package com.rmnnorbert.dentocrates.dto.clinic.leave;

public record LeaveDeleteDTO(long dentistId,
                            long leaveId,
                            long clinicId
                            ) {
}
