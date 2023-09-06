package com.rmnnorbert.dentocrates.controller.dto;

import jakarta.validation.constraints.Min;

public record DeleteDTO(@Min(1) long userId,
                        @Min(1) long targetId
                        ) {
}
