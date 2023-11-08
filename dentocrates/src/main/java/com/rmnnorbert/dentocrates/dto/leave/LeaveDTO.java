package com.rmnnorbert.dentocrates.dto.leave;

import com.rmnnorbert.dentocrates.dao.leave.Leave;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record LeaveDTO(@Min(1) long id,
                       @Min(1) long clinicId,
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
