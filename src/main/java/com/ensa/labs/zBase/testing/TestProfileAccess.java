package com.ensa.labs.zBase.testing;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

@Component
public class TestProfileAccess {
    public static final String ACCESS_HEADER = "X-Ensa-Test-Access";

    private static final Pattern TEST_USERNAME =
            Pattern.compile("ensa-test-(super-admin|lab-director|team-leader|researcher)-(a|b)");
    private static final Pattern ACCESS_TOKEN = Pattern.compile("[0-9a-fA-F]{64}");

    private final byte[] accessToken;

    public TestProfileAccess(@Value("${ENSA_TEST_PROFILES_ENABLED:false}") boolean enabled,
                             @Value("${ENSA_TEST_ACCESS_TOKEN:}") String accessToken,
                             @Value("${security.enabled:true}") boolean securityEnabled) {
        this.accessToken = enabled && securityEnabled && ACCESS_TOKEN.matcher(accessToken).matches()
                ? accessToken.getBytes(StandardCharsets.US_ASCII)
                : null;
    }

    public boolean isEnabled() {
        return accessToken != null;
    }

    public static boolean isTestAccount(String username) {
        return username != null && TEST_USERNAME.matcher(username).matches();
    }

    public boolean hasAccess(HttpServletRequest request) {
        if (!isEnabled() || request == null) {
            return false;
        }
        String submittedToken = request.getHeader(ACCESS_HEADER);
        return submittedToken != null && submittedToken.length() == accessToken.length
                && MessageDigest.isEqual(accessToken, submittedToken.getBytes(StandardCharsets.US_ASCII));
    }

    public void requireAccountAccess(String username) {
        if (isTestAccount(username)
                && (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)
                || !hasAccess(attributes.getRequest()))) {
            throw new BadCredentialsException("Test account access denied");
        }
    }
}
