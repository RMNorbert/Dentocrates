package com.rmnnorbert.dentocrates.service.client.oauth2;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.security.auth.TokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
@Service
public class OAuth2HelperService {
    private final String id = System.getenv("oauthId");
    private final String secret = System.getenv("oauthSecret");
    private final String redirectUri = System.getenv("REDIRECT_URI");

    public ResponseEntity<TokenResponse> getGoogleTokenResponse(String code) {
        String oauthTokenUrl = "https://www.googleapis.com/oauth2/v4/token";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", id);
        requestBody.add("client_secret", secret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        // Set the headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HTTP entity with the headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request to the token endpoint
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                oauthTokenUrl,
                HttpMethod.POST,
                requestEntity,
                TokenResponse.class
        );
    }
    public String[] parseToken(String idToken) {
        try {
            // Parse the ID token
            JWT jwt = JWTParser.parse(idToken);

            if (jwt instanceof SignedJWT signedJWT) {
                // Retrieve the claims set from the token
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                // Extract user attributes from the claims set
                String userId = claimsSet.getSubject();
                String email = claimsSet.getStringClaim("email");
                String[] name = claimsSet.getStringClaim("name").split(" ");
                return new String[]{email,userId,name[0],name[1]};
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        throw new InvalidCredentialException();
    }

    public String[] getOauthCredentials(String generatedState , String state, String code) {
        if (state.equals(generatedState)){
            ResponseEntity<TokenResponse> responseEntity = getGoogleTokenResponse(code);
            // Access the returned token and other fields from tokenResponse
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                TokenResponse tokenResponse = responseEntity.getBody();
                String idToken = tokenResponse.getId_token();
                return parseToken(idToken);
            } else {
                System.out.println("Token exchange failed. Status code: " + responseEntity.getStatusCode());
                throw new InvalidCredentialException();
            }
        } else {
            throw new InvalidCredentialException();
        }
    }
}
