package com.rmnnorbert.dentocrates.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static com.rmnnorbert.dentocrates.data.Role.*;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.requestMatchers(
                        "/api/**",
                        "/",
                        "/frontend/index",
                        "/index.html",
                        "/static/**",
                        "/*.ico",
                        "/*.json",
                        "/*.png",
                        "/frontend/**",
                        "/clinic/all",
                        "/home",
                        "/actuator/**",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/api-docs/**",
                        "/api-docs.yaml"
                    ).permitAll()
                    .requestMatchers("/client/{id}",
                        "/calendar/**",
                        "/client/",
                        "/clinic/{id}",
                        "/dentist/all",
                        "/dentist/{id}",
                        "/location/all",
                        "/leave/{id}"
                     ).hasAnyRole(ADMIN.name(), CUSTOMER.name(), DENTIST.name())
                    .requestMatchers("/client/**",
                        "/clinic/**",
                        "/dentist/**",
                        "/location/**",
                        "/leave/**"
                    ).hasAnyRole(ADMIN.name(), DENTIST.name())
                    .anyRequest()
                    .authenticated())
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

