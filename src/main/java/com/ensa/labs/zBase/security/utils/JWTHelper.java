package com.ensa.labs.zBase.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.ensa.labs.zBase.security.constant.JWTUtil.*;

@Component
public class JWTHelper {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JWTHelper(@Value("${app.security.jwt.secret}") String secret,
                     @Value("${app.security.jwt.issuer:ensa-labs}") String issuer,
                     @Value("${app.security.jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
                     @Value("${app.security.jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("app.security.jwt.secret must be at least 32 characters long");
        }
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .withIssuer(issuer)
                .withClaim(ROLES_CLAIM, roles)
                .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .sign(algorithm);
    }

    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .withIssuer(issuer)
                .withClaim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .sign(algorithm);
    }

    /**
     * Verifies signature, issuer, expiry and token type.
     *
     * @throws JWTVerificationException when the token is invalid
     */
    public DecodedJWT verify(String token, String expectedType) {
        DecodedJWT decoded = verifier.verify(token);
        String type = decoded.getClaim(TOKEN_TYPE_CLAIM).asString();
        if (!expectedType.equals(type)) {
            throw new JWTVerificationException("Unexpected token type");
        }
        return decoded;
    }

    public String extractTokenFromHeaderIfExists(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }
}
