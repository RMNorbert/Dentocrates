package com.rmnnorbert.dentocrates.dao.clinic;

import com.rmnnorbert.dentocrates.controller.dto.clinic.LocationDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_seq"
    )
    @SequenceGenerator(
            name = "location_seq",
            sequenceName = "location_seq",
            allocationSize = 1
    )
    private Long id;
    @Column(unique = true)
    private final int zipCode;
    @Column(unique = true)
    private final String city;

    public static Location of(LocationDTO locationDTO) {
        return Location.builder()
                .zipCode(locationDTO.getZipCode())
                .city(locationDTO.getCity())
                .build();
    }
}
