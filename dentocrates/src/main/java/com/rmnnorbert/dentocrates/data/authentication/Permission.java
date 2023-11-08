package com.rmnnorbert.dentocrates.data.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    CLIENT_READ("client:read"),
    CLIENT_UPDATE("client:update"),
    CLIENT_CREATE("client:create"),
    CLIENT_DELETE("client:delete"),

    CLINIC_READ("clinic:read"),
    CLINIC_UPDATE("clinic:update"),
    CLINIC_CREATE("clinic:create"),
    CLINIC_DELETE("clinic:delete"),

    LOCATION_READ("location:read"),
    LOCATION_UPDATE("location:update"),
    LOCATION_CREATE("location:create"),
    LOCATION_DELETE("location:delete"),

    APPOINTMENT_READ("appointment:read"),
    APPOINTMENT_UPDATE("appointment:update"),
    APPOINTMENT_CREATE("appointment:create"),
    APPOINTMENT_DELETE("appointment:delete");
    @Getter
    private final String permission;
}
