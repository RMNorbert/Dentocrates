package com.rmnnorbert.dentocrates.controller.dto.clinic.leave;

import com.rmnnorbert.dentocrates.dao.clinic.Leave;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record LeaveDTO(long id,
                       long clinicId,
                       LocalDateTime startOfTheLeave,
                       LocalDateTime endOfTheLeave
                      ) {
    public static LeaveDTO of (Leave leave) {
        return LeaveDTO.builder()
                       .id(leave.getId())
                       .clinicId(leave.getClinic().getId())
                       .startOfTheLeave(leave.getStartOfTheLeave())
                       .endOfTheLeave(leave.getEndOfTheLeave())
                       .build();
    }
}
