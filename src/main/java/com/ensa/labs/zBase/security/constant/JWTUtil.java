package com.ensa.labs.zBase.security.constant;

/**
 * Static JWT constants. Secrets and lifetimes are configured in application.yml
 * (app.security.jwt.*) and read by {@link com.ensa.labs.zBase.security.utils.JWTHelper}.
 */
public final class JWTUtil {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String ROLES_CLAIM = "roles";
    public static final String TOKEN_TYPE_CLAIM = "type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";

    private JWTUtil() {
    }
}
