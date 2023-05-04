package com.rmnnorbert.dentocrates.dao.client;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Customer extends Client {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_seq"
    )
    @SequenceGenerator(
            name = "customer_seq",
            sequenceName = "customer_seq",
            allocationSize = 1
    )
    private final Long id;

}
