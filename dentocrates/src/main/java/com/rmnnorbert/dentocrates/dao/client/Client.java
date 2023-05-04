package com.rmnnorbert.dentocrates.dao.client;

import lombok.*;


@Getter
@NonNull
@NoArgsConstructor(force = true)
@AllArgsConstructor
public abstract class Client {
    protected final String email;
    protected final String password;
    protected final String firstName;
    protected final String lastName;
    protected final int authorizationCategory;

}
