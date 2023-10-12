export const handleGoogleLogin = async () => {
        try {
            const googleRedirectUrl ="/api/oauth2/authorizationPageUrl/google";
            const response = await fetch(googleRedirectUrl);
            console.log(response)
            if (response.ok) {
                const url = await response.text();
                window.location.replace(url);
            } else {
                console.error("Failed to fetch Google authorization URL");
            }
        } catch (error) {
            console.error("Error fetching Google authorization URL:", error);
        }
}
