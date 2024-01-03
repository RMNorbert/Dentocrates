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
    private final static String ID = System.getenv("OAUTH_ID");
    private final static String SECRET = System.getenv("OAUTH_SECRET");
    private final static String REDIRECT_URI = System.getenv("REDIRECT_URI");
    private final static String OAUTH_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    private final static String EMAIL_CLAIM = "email";
    private final static String NAME_CLAIM = "name";
    private final static int FIRST_NAME_INDEX = 0;
    private final static int LAST_NAME_INDEX = 1;

    public ResponseEntity<TokenResponse> getGoogleTokenResponse(String code) {
        MultiValueMap<String, String> requestBody = buildRequestBody(code);

        // Set the headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HTTP entity with the headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request to the token endpoint
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                OAUTH_TOKEN_URL,
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
                String email = claimsSet.getStringClaim(EMAIL_CLAIM);
                String[] name = claimsSet.getStringClaim(NAME_CLAIM).split(" ");
                return new String[]{email,userId,name[FIRST_NAME_INDEX],name[LAST_NAME_INDEX]};
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
    private MultiValueMap<String, String> buildRequestBody(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", ID);
        requestBody.add("client_secret", SECRET);
        requestBody.add("redirect_uri", REDIRECT_URI);
        requestBody.add("grant_type", "authorization_code");

        return requestBody;
    }
}
