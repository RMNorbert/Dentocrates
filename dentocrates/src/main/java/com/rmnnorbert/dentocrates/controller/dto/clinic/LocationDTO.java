package com.rmnnorbert.dentocrates.controller.dto.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Objects;
@Builder
public record LocationDTO(@Min(1000) @Max(10000) int zipCode, @NotBlank String city) {
    public static LocationDTO of(Location location) {
        return LocationDTO.builder()
                .zipCode(location.getZipCode())
                .city(location.getCity())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LocationDTO) obj;
        return this.zipCode == that.zipCode &&
                Objects.equals(this.city, that.city);
    }

    @Override
    public String toString() {
        return "LocationDTO[" +
                "zipCode=" + zipCode + ", " +
                "city=" + city + ']';
    }

}
