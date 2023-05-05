package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.controller.dto.client.DentistRegisterDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NonNull
@NoArgsConstructor(force = true)
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
    @Column(unique = true)
    private final String operatingLicenceNo;
    @Transient
    private static final int DENTIST_AUTHORIZATION_CATEGORY = 2;
    @Builder
    public Dentist(String email, String password, String firstName, String lastName, Long id, String operatingLicenceNo) {
        super(email, password, firstName, lastName, DENTIST_AUTHORIZATION_CATEGORY);
        this.id = id;
        this.operatingLicenceNo = operatingLicenceNo;
    }
    public static Dentist of(DentistRegisterDTO dentistRegisterDTO){
        return Dentist.builder()
                .email(dentistRegisterDTO.email())
                .password(dentistRegisterDTO.password())
                .firstName(dentistRegisterDTO.firstName())
                .lastName(dentistRegisterDTO.lastname())
                .build();
    }
}
