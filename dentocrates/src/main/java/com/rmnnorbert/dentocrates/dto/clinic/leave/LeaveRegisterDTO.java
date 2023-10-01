package com.rmnnorbert.dentocrates.dto.clinic.leave;

import java.time.LocalDateTime;

public record LeaveRegisterDTO (long clinicId,
                                LocalDateTime startOfTheLeave,
                                LocalDateTime endOfTheLeave
                               ){
}
