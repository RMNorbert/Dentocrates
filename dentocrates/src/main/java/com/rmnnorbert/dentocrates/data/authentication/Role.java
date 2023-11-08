package com.rmnnorbert.dentocrates.data.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rmnnorbert.dentocrates.data.authentication.Permission.*;

@RequiredArgsConstructor
public enum Role {
    CUSTOMER(Set.of(
            LOCATION_READ,
            APPOINTMENT_READ
    )),
    DENTIST(Set.of(
            CLIENT_READ,
            CLIENT_CREATE,
            CLIENT_UPDATE,
            CLINIC_READ,
            CLINIC_CREATE,
            CLINIC_UPDATE,
            LOCATION_READ,
            LOCATION_CREATE,
            LOCATION_UPDATE,
            APPOINTMENT_READ,
            APPOINTMENT_CREATE,
            APPOINTMENT_UPDATE
    )),
    ADMIN(Arrays.stream(Permission.values()).collect(Collectors.toSet()));

    @Getter
    private final Set<Permission> permissions;
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
