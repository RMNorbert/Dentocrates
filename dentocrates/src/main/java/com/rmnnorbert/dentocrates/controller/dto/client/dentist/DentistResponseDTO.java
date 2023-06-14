package com.rmnnorbert.dentocrates.controller.dto.client.dentist;

import com.rmnnorbert.dentocrates.dao.client.Dentist;
import com.rmnnorbert.dentocrates.data.Role;
import lombok.Builder;

@Builder
public record DentistResponseDTO(String email,
                                 String firstName,
                                 String lastname,
                                 Role role,
                                 String operatingLicenceNo) {
    public static DentistResponseDTO of(Dentist dentist) {
        return DentistResponseDTO.builder()
                .email(dentist.getEmail())
                .firstName(dentist.getFirstName())
                .lastname(dentist.getLastName())
                .role(dentist.getRole())
                .operatingLicenceNo(dentist.getOperatingLicenceNo())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentistResponseDTO that = (DentistResponseDTO) o;
        return role == that.role && email.equals(that.email) && firstName.equals(that.firstName) && lastname.equals(that.lastname) && operatingLicenceNo.equals(that.operatingLicenceNo);
    }

}
