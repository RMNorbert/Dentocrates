package com.rmnnorbert.dentocrates.security.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    /** The starting index of JWT (JSON Web Token) within the Authorization header. */
    private final static int JWT_BEGIN_INDEX = 7;
    /** The name of the HTTP header field that carries the Authorization information. */
    private final static String AUTHORIZATION_HEADER = "Authorization";
    /** The prefix used in the Authorization header to indicate the type of credentials provided (Bearer token). */
    private final static String BEARER_PREFIX = "Bearer ";

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is called by the servlet container each time a request/response pair is passed through the filter
     * chain due to a client request for a resource at the end of the chain. It extracts the JWT from the Authorization
     * header, validates it, and sets up the Spring Security context if the token is valid.
     *
     * @param request       The incoming HttpServletRequest.
     * @param response      The outgoing HttpServletResponse.
     * @param filterChain   A FilterChain through which the request and response flow.
     * @throws ServletException If an exception occurs that interrupts the normal operation of the filter.
     * @throws IOException      If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header from the incoming request.
        final String autHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Check if the Authorization header is missing or doesn't start with the Bearer prefix.
        if (autHeader == null || !autHeader.startsWith(BEARER_PREFIX)) {
            // If missing or not a Bearer token, continue with the filter chain.
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT (without Bearer prefix) from the Authorization header.
        String jwt = autHeader.substring(JWT_BEGIN_INDEX);
        String userEmail = jwtService.extractUsername(jwt);

        // Check if the user email is not null and there is no existing authentication in the SecurityContextHolder.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Load user details from the userDetailsService based on the user email.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Check if the JWT is valid for the loaded user details.
            if (jwtService.isTokenValid(jwt,userDetails)){
                // Create an authentication token and set up the SecurityContextHolder.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the filter chain.
        filterChain.doFilter(request,response);
    }
}
