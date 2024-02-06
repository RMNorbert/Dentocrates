package com.rmnnorbert.dentocrates.service.client.oauth2;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidCredentialException;
import com.rmnnorbert.dentocrates.custom.exceptions.InvalidOAuth2ClientRegistrationException;
import com.rmnnorbert.dentocrates.custom.exceptions.OAuth2AuthenticationException;
import com.rmnnorbert.dentocrates.security.auth.TokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
@Service
public class OAuth2HelperService {
    /** Google OAuth client ID obtained from environment variables */
    private final static String ID = System.getenv("OAUTH_ID");
    /** Google OAuth client secret obtained from environment variables */
    private final static String SECRET = System.getenv("OAUTH_SECRET");
    /** Redirect URI for Google OAuth callback obtained from environment variables */
    private final static String REDIRECT_URI = System.getenv("REDIRECT_URI");
    /** URL for exchanging the authorization code with Google OAuth for an access token */
    private final static String OAUTH_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    /** Claim name for user email in the Google OAuth ID token */
    private final static String EMAIL_CLAIM = "email";
    /** Claim name for username in the Google OAuth ID token */
    private final static String NAME_CLAIM = "name";
    /** Index of the first name in the name claim split array */
    private final static int FIRST_NAME_INDEX = 0;
    /** Index of the last name in the name claim split array */
    private final static int LAST_NAME_INDEX = 1;

    /**
     * Retrieves OAuth credentials using the provided authorization code.
     *
     * @param generatedState The state value generated during the initial authentication request.
     * @param state          The state value received in the callback, which should match the generated state.
     * @param code           The authorization code received in the callback.
     * @return An array containing OAuth credentials.
     * @throws InvalidCredentialException If the state values do not match or if the token exchange fails.
     */
    public String[] getOauthCredentials(String generatedState , String state, String code) {
        if (state.equals(generatedState)){
            ResponseEntity<TokenResponse> responseEntity = getGoogleTokenResponse(code);
            // Access the returned token and other fields from tokenResponse
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                TokenResponse tokenResponse = responseEntity.getBody();
                String idToken = tokenResponse.getId_token();
                return parseToken(idToken);
            } else {
                throw new InvalidCredentialException();
            }
        } else {
            throw new InvalidCredentialException();
        }
    }

    /**
     * Retrieves the Google OAuth token response by exchanging the provided authorization code.
     *
     * @param code The authorization code received during the OAuth authorization flow.
     * @return ResponseEntity containing the Google OAuth token response.
     * @throws OAuth2AuthenticationException if an error occurs during the authentication process.
     * @throws InvalidOAuth2ClientRegistrationException if there is an issue with the OAuth2 client registration.
     */
    private ResponseEntity<TokenResponse> getGoogleTokenResponse(String code) {
        try {
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
        } catch (HttpClientErrorException ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        } catch (Exception e) {
            throw new InvalidOAuth2ClientRegistrationException(e.getMessage());
        }
    }

    /**
     * Parses the provided ID token, extracting user attributes such as email, user ID, first name, and last name.
     *
     * @param idToken The ID token to be parsed.
     * @return An array containing user attributes extracted from the ID token.
     *         Index 0: User email
     *         Index 1: User ID
     *         Index 2: User first name
     *         Index 3: User last name
     * @throws InvalidCredentialException If there is an issue parsing the ID token.
     */
    private String[] parseToken(String idToken) {
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

    /**
     * Builds the request body for exchanging an authorization code in a Google OAuth token request.
     *
     * @param code The authorization code received during the OAuth authorization flow.
     * @return MultiValueMap representing the request body with required parameters.
     */
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
