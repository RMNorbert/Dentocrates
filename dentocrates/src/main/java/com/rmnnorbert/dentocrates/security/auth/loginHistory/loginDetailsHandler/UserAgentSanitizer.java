package com.rmnnorbert.dentocrates.security.auth.loginHistory.loginDetailsHandler;

import org.springframework.stereotype.Service;

@Service
public class UserAgentSanitizer {
    /**
     * Sanitizes the user agent string by removing unwanted characters and converting to lowercase.
     *
     * @param userAgent The user agent string to be sanitized.
     * @return The sanitized user agent string.
     */
    public String sanitizeUserAgent(String userAgent) {
        if (userAgent == null) {
            return "";
        }
        userAgent = userAgent.replaceAll("[^a-zA-Z0-9 .-]", "").trim().toLowerCase();
        return userAgent;
    }
}
