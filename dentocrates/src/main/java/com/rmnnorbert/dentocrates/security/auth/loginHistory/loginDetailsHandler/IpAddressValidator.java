package com.rmnnorbert.dentocrates.security.auth.loginHistory.loginDetailsHandler;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class IpAddressValidator {
    /** Regular expression pattern for matching full IPv6 addresses. */
    private static final Pattern IPV6_PATTERN = Pattern
            .compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    /** Regular expression pattern for matching compressed IPv6 addresses. */
    private static final Pattern IPV6_COMPRESSED_PATTERN = Pattern
            .compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    public boolean isValidIPAddress(String ipAddress) {
        if(ipAddress != null) {
            String[] ipAddressAsArray = ipAddress.split("[.:]");

            if (ipAddressAsArray.length == 4) {
                return validateIpV4Address(ipAddressAsArray);
            } else if (ipAddressAsArray.length <= 8) {
                return validateIpV6Address(ipAddress);
            }
        }
        return false;
    }

    /**
     * Validates an IPv4 address represented as an array of strings.
     * Each element in the array should be a valid integer in the range [0, 255].
     *
     * @param ipAddress An array representing an IPv4 address.
     * @return true if the IPv4 address is valid, false otherwise.
     */
    private boolean validateIpV4Address(String[] ipAddress) {
        for (String str : ipAddress) {
            try {
                int intValue = Integer.parseInt(str);
                if (intValue < 0 || intValue > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates an IPv6 address.
     *
     * @param ipAddress The IPv6 address to be validated.
     * @return true if the IPv6 address is valid according to the patterns, false otherwise.
     */
    private boolean validateIpV6Address(String ipAddress) {
        Matcher matcher = IPV6_PATTERN.matcher(ipAddress);
        if (matcher.matches()) {
            return true;
        }
        return IPV6_COMPRESSED_PATTERN.matcher(ipAddress).matches();
    }
}
