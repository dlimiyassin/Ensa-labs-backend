package com.ensa.labs.zBase.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ensa.labs.zBase.security.utils.JWTHelper;
import com.ensa.labs.zBase.testing.TestProfileAccess;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.ensa.labs.zBase.security.constant.JWTUtil.*;

/**
 * Reads the Bearer access token (if any) and populates the security context.
 * An invalid or expired token never blocks the request by itself: the request simply
 * continues anonymously, so public endpoints keep working and protected endpoints
 * answer 401 through the configured authentication entry point.
 * Synthetic accounts also require a tester session and are limited to read requests.
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Set<String> READ_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    private final JWTHelper jwtHelper;
    private final TestProfileAccess testProfileAccess;

    public JWTAuthorizationFilter(JWTHelper jwtHelper, TestProfileAccess testProfileAccess) {
        this.jwtHelper = jwtHelper;
        this.testProfileAccess = testProfileAccess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtHelper.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
        if (accessToken != null) {
            try {
                DecodedJWT decodedJWT = jwtHelper.verify(accessToken, ACCESS_TOKEN_TYPE);
                String username = decodedJWT.getSubject();
                if (TestProfileAccess.isTestAccount(username)) {
                    if (!testProfileAccess.hasAccess(request)) {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    if (!READ_METHODS.contains(request.getMethod())
                            || "/api/publications/duplicates".equals(request.getServletPath())) {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
                List<String> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(String.class);
                List<SimpleGrantedAuthority> authorities = roles == null ? List.of()
                        : roles.stream().map(SimpleGrantedAuthority::new).toList();
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(username, null, authorities));
            } catch (JWTVerificationException e) {
                logger.debug("JWT verification failed: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
