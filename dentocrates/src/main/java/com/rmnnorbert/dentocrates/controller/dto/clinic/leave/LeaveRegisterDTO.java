package com.rmnnorbert.dentocrates.controller.dto.clinic.leave;

import java.time.LocalDateTime;

public record LeaveRegisterDTO (long clinicId,
                                LocalDateTime startOfTheLeave,
                                LocalDateTime endOfTheLeave
                               ){
}
