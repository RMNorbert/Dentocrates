package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.location.LocationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1000)
    @Max(10000)
    @Column(unique = true)
    private final int zipCode;

    @NotBlank
    private final String city;

    public static Location of(LocationDTO locationDTO) {
        return Location.builder()
                .zipCode(locationDTO.zipCode())
                .city(locationDTO.city())
                .build();
    }
}
