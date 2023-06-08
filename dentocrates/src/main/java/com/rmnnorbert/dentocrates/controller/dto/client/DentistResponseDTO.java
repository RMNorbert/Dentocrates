package com.rmnnorbert.dentocrates.controller.dto.client;

import com.rmnnorbert.dentocrates.dao.client.Dentist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class DentistResponseDTO {
    private final String email;
    private final String firstName;
    private final String lastname;
    private final int authorizationCategory;
    private final String operatingLicenceNo;
    public static DentistResponseDTO of(Dentist dentist){
        return DentistResponseDTO.builder()
                .email(dentist.getEmail())
                .firstName(dentist.getFirstName())
                .lastname(dentist.getLastName())
                .authorizationCategory(dentist.getAuthorizationCategory())
                .operatingLicenceNo(dentist.getOperatingLicenceNo())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentistResponseDTO that = (DentistResponseDTO) o;
        return authorizationCategory == that.authorizationCategory && email.equals(that.email) && firstName.equals(that.firstName) && lastname.equals(that.lastname) && operatingLicenceNo.equals(that.operatingLicenceNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastname, authorizationCategory, operatingLicenceNo);
    }
}
