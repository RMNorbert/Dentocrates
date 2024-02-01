package com.rmnnorbert.dentocrates.security.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    /** Secret key used for signing JWTs. It is retrieved from the environment variable "SECRET" */
    private static final String SECRET_KEY = System.getenv("SECRET");
    /** Duration, in milliseconds, for which the generated JWTs are considered valid.
     * The default expiration is set to 2.8 hours which is 168 minutes. */
    private static final int EXPIRATION = 1000 * 60 * 24 *7;

    /**
     * Extracts all claims from a JWT token by parsing it using the configured signing key.
     *
     * @param token The JWT token from which claims will be extracted.
     * @return A Claims object containing all the extracted claims from the JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a JWT token is valid by comparing the username extracted from the token with the
     * username from the provided UserDetails and verifying that the token has not expired.
     *
     * @param token The JWT token to be validated.
     * @param userDetails The UserDetails object representing the user details for comparison.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token using the provided claimsResolver function.
     *
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver A function to resolve the desired claim from the Claims object.
     * @param <T> The type of the extracted claim.
     * @return The extracted claim of type T.
     * (The actual type is determined by the claimsResolver when the method is invoked)
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    /**
     * Generates a JWT (JSON Web Token) for the given extra claims and user details.
     *
     * @param extraClaims Additional claims to include in the JWT payload.
     * @param userDetails Details of the user for whom the token is being generated.
     * @return The generated JWT as a string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key used for generating JWTs by decoding the BASE64-encoded
     * secret key obtained from the environment variable "SECRET".
     *
     * @return The signing key for JWT generation.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

