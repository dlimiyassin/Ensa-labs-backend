package com.ensa.labs.zBase.security.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ensa.labs.zBase.security.constant.JWTUtil.ACCESS_TOKEN_TYPE;
import static com.ensa.labs.zBase.security.constant.JWTUtil.REFRESH_TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JWTHelperTest {

    private static final String SECRET = "unit-test-secret-with-at-least-32-characters";

    private final JWTHelper helper = new JWTHelper(SECRET, "ensa-labs", 60_000, 120_000);

    @Test
    void accessTokenCarriesSubjectAndRoles() {
        String token = helper.generateAccessToken("admin", List.of("ROLE_SUPER_ADMIN"));

        DecodedJWT decoded = helper.verify(token, ACCESS_TOKEN_TYPE);

        assertThat(decoded.getSubject()).isEqualTo("admin");
        assertThat(decoded.getClaim("roles").asList(String.class)).containsExactly("ROLE_SUPER_ADMIN");
    }

    @Test
    void refreshTokenCannotBeUsedAsAccessToken() {
        String refresh = helper.generateRefreshToken("admin");

        assertThat(helper.verify(refresh, REFRESH_TOKEN_TYPE).getSubject()).isEqualTo("admin");
        assertThatThrownBy(() -> helper.verify(refresh, ACCESS_TOKEN_TYPE)).isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void tokenSignedWithAnotherSecretIsRejected() {
        JWTHelper other = new JWTHelper("another-secret-with-at-least-32-characters!!", "ensa-labs", 60_000, 120_000);
        String foreign = other.generateAccessToken("admin", List.of("ROLE_SUPER_ADMIN"));

        assertThatThrownBy(() -> helper.verify(foreign, ACCESS_TOKEN_TYPE)).isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void expiredTokenIsRejected() {
        JWTHelper shortLived = new JWTHelper(SECRET, "ensa-labs", -1_000, -1_000);
        String expired = shortLived.generateAccessToken("admin", List.of());

        assertThatThrownBy(() -> helper.verify(expired, ACCESS_TOKEN_TYPE)).isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void shortSecretIsRefused() {
        assertThatThrownBy(() -> new JWTHelper("too-short", "ensa-labs", 1, 1)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void extractsBearerToken() {
        assertThat(helper.extractTokenFromHeaderIfExists("Bearer abc")).isEqualTo("abc");
        assertThat(helper.extractTokenFromHeaderIfExists("Basic abc")).isNull();
        assertThat(helper.extractTokenFromHeaderIfExists(null)).isNull();
    }
}
