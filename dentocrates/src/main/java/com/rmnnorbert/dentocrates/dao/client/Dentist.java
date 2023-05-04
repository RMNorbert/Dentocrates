package com.rmnnorbert.dentocrates.dao.client;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Dentist extends Client {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dentist_seq"
    )
    @SequenceGenerator(
            name = "dentist_seq",
            sequenceName = "dentist_seq",
            allocationSize = 1
    )
    private final Long id;
    private final String operatingLicence;
}
