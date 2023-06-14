package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.controller.dto.client.dentist.DentistRegisterDTO;
import com.rmnnorbert.dentocrates.data.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(force = true)
public class Dentist extends Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(unique = true)
    @NotBlank
    private final String operatingLicenceNo;
    @Transient
    private static final Role DENTIST_ROLE = Role.DENTIST;
    @Builder
    public Dentist(String email, String password, String firstName, String lastName, Long id, String operatingLicenceNo) {
        super(email, password, firstName, lastName, DENTIST_ROLE);
        this.id = id;
        this.operatingLicenceNo = operatingLicenceNo;
    }
    public static Dentist of(DentistRegisterDTO dentistRegisterDTO){
        return Dentist.builder()
                .email(dentistRegisterDTO.email())
                .password(dentistRegisterDTO.password())
                .firstName(dentistRegisterDTO.firstName())
                .lastName(dentistRegisterDTO.lastname())
                .operatingLicenceNo(dentistRegisterDTO.operatingLicenceNo())
                .build();
    }
}
