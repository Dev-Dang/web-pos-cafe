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
 * @version 1.1.0
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
public final class SecurityKeys {

    private SecurityKeys() {
    }

    // ============================================
    // TOKEN KEYS
    // ============================================
    public static final String TOKEN_AUTH = "x-auth";
    public static final String TOKEN_DEVICE_ID = "x-auth-device";

    // ============================================
    // CSRF PROTECTION
    // ============================================
    public static final String CSRF_TOKEN_NAME = "x-csrf-token";           // Cookie name
    public static final String CSRF_HEADER_NAME = "x-csrf-token";        // Attribute header
    public static final String CSRF_PARAM_NAME = "_csrf";                // Form parameter
    public static final String SESSION_CSRF_TOKEN = "csrfToken";         // Session attribute

    // ============================================
    // COOKIE CONFIGURATION
    // ============================================
    public static final String AUTH_COOKIE_PREFIX = "x-auth";
    public static final String COOKIE_PATH = "/";
    public static final int CSRF_TOKEN_MAX_AGE = -1;                     // Session cookie

    // ============================================
    // SESSION ATTRIBUTES
    // ============================================
    public static final String SESSION_AUTH_USER = "authUser";
    public static final String SESSION_AUTH_TOKENS = "authTokens";
    public static final String SESSION_AUTH_SESSION = "authSessionInfo";

    // ============================================
    // CONTEXT ATTRIBUTES
    // ============================================
    public static final String CTX_AUTH_SESSION_MANAGER = "authSessionManager";

    // ============================================
    // SESSION DURATIONS (minutes)
    // ============================================
    public static final int REFRESH_MINUTES = 15;
    public static final int SUPER_ADMIN_SESSION_TTL = 30;
    public static final int MANAGER_SESSION_TTL = 240;
    public static final int STAFF_SESSION_TTL = -1;
    public static final int DEFAULT_SESSION_TTL = 10080;
    public static final int RESET_PASSWORD_TOKEN_TTL_MINUTES = 15;
}
