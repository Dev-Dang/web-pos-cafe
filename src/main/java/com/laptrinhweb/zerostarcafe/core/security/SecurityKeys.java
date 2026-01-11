package com.laptrinhweb.zerostarcafe.core.security;

/**
 * <h2>Description:</h2>
 * <p>
 * Centralizes authentication constants.
 * This class defines keys for cookies, session attributes,
 * request attributes, and application-scoped attributes
 * to improve maintainability and avoid magic strings.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
public final class SecurityKeys {

    private SecurityKeys() {
    }

    // AuthToken keys
    public static final String TOKEN_AUTH = "x-auth";
    public static final String TOKEN_DEVICE_ID = "x-auth-device";

    // Cookie keys
    public static final String AUTH_COOKIE_PREFIX = "x-auth";

    // Session attribute keys
    public static final String SESSION_AUTH_USER = "authUser";
    public static final String SESSION_AUTH_TOKENS = "authTokens";
    public static final String SESSION_AUTH_SESSION = "authSessionInfo";

    // Servlet context keys
    public static final String CTX_AUTH_SESSION_MANAGER = "authSessionManager";

    // Session durations (in minutes)
    public static final int REFRESH_MINUTES = 15;
    public static final int SUPER_ADMIN_SESSION_TTL = 30;
    public static final int MANAGER_SESSION_TTL = 240;      // 4 hours
    public static final int STAFF_SESSION_TTL = -1;         // end of day (special case)
    public static final int DEFAULT_SESSION_TTL = 10080;    // 7 days
}
