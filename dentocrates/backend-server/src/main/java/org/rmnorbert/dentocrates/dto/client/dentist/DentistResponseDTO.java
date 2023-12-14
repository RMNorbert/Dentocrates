package org.rmnorbert.dentocrates.dto.client.dentist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.rmnorbert.dentocrates.dao.client.Dentist;
import org.rmnorbert.dentocrates.data.authentication.Role;

@Builder
public record DentistResponseDTO(@Min(1) long id,
                                 @NotBlank String email,
                                 @NotBlank String firstName,
                                 @NotBlank String lastName,
                                  Role role,
                                 @NotBlank String operatingLicenceNo) {
    public static DentistResponseDTO of(Dentist dentist) {
        return DentistResponseDTO.builder()
                .id(dentist.getId())
                .email(dentist.getEmail())
                .firstName(dentist.getFirstName())
                .lastName(dentist.getLastName())
                .role(dentist.getRole())
                .operatingLicenceNo(dentist.getOperatingLicenceNo())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentistResponseDTO that = (DentistResponseDTO) o;
        return role == that.role && email.equals(that.email) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && operatingLicenceNo.equals(that.operatingLicenceNo);
    }

}
