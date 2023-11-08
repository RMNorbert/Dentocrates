package com.rmnnorbert.dentocrates.dto;

import jakarta.validation.constraints.Min;

public record DeleteDTO(@Min(1) long userId,
                        @Min(1) long targetId
                        ) {
}
