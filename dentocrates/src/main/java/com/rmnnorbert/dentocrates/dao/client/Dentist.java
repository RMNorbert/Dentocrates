package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.authentication.Role;
import com.rmnnorbert.dentocrates.dto.client.dentist.DentistRegisterDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@DiscriminatorValue("DENTIST")
@Entity
public class Dentist extends Client {
    @Column(unique = true)
    @NotBlank
    private final String operatingLicenceNo;
    @Builder
    public Dentist(Long id, String email, String password, String firstName, String lastName, String operatingLicenceNo, Role role) {
        super(email, password, firstName, lastName, role);
        this.operatingLicenceNo = operatingLicenceNo;
        this.id = id;
    }

    public static Dentist toEntity(DentistRegisterDTO request, String password){
        return Dentist.builder()
                .email(request.email())
                .password(password)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.DENTIST)
                .operatingLicenceNo(request.operatingLicenceNo())
                .build();
    }
}
