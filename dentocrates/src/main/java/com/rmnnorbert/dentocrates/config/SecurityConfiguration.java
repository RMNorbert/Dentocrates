package com.rmnnorbert.dentocrates.config;

import com.rmnnorbert.dentocrates.security.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.rmnnorbert.dentocrates.data.authentication.Role.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final static String ID = System.getenv("OAUTH_ID");
    private final static String SECRET = System.getenv("OAUTH_SECRET");

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
                                        "/api/register/**",
                                        "/api/authenticate",
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
                                        "/api-docs.yaml",
                                        "/oauth2/**",
                                        "/login/**",
                                        "/api/**",
                                        "/login/oauth2/**",
                                        "/update",
                                        "/chat/**",
                                        "/contact/**"
                                ).permitAll()
                                .requestMatchers("/client/{id}",
                                        "/verify/**",
                                        "/api/reset",
                                        "/api/verify",
                                        "/calendar/**",
                                        "/client/",
                                        "/clinic/{id}",
                                        "/dentist/all",
                                        "/dentist/{id}",
                                        "/location/all",
                                        "/leave/{id}",
                                        "/review/**"
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
                .oauth2Login(Customizer.withDefaults())
                .build();
    }
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(ID)
                .clientSecret(SECRET)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
}

