package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.location.LocationDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Min(1000)
    @Max(10000)
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
