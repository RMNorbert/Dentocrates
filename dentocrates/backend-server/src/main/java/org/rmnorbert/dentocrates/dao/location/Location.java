package org.rmnorbert.dentocrates.dao.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.rmnorbert.dentocrates.dto.location.LocationDTO;

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

    private final double longitude;

    private final double latitude;

    public static Location of(LocationDTO locationDTO, double longitude, double latitude) {
        return Location.builder()
                .zipCode(locationDTO.zipCode())
                .city(locationDTO.city())
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
