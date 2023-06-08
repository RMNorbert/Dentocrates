package com.rmnnorbert.dentocrates.controller.dto.clinic;

import com.rmnnorbert.dentocrates.dao.clinic.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
@Getter
@Builder
@AllArgsConstructor
public final class LocationDTO {
    private final int zipCode;
    private final String city;

    public static LocationDTO of(Location location){
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
    public int hashCode() {
        return Objects.hash(zipCode, city);
    }

    @Override
    public String toString() {
        return "LocationDTO[" +
                "zipCode=" + zipCode + ", " +
                "city=" + city + ']';
    }

}
