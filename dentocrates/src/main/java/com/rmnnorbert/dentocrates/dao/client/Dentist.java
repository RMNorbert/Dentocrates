package com.rmnnorbert.dentocrates.dao.client;

import com.rmnnorbert.dentocrates.data.Role;
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

}
