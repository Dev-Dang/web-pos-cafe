package com.laptrinhweb.zerostarcafe.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * <h2>Description:</h2>
 * <p>
 * CSRF token manager using Double Submit Cookie pattern. Provides secure token
 * generation, validation, and management for preventing Cross-Site Attribute Forgery attacks.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Get or generate token for safe methods
 * String token = CsrfTokenUtils.getOrCreate(request, response);
 *
 * // Validate token for state-changing methods
 * boolean valid = CsrfTokenUtils.verifyToken(request);
 *
 * // Clear token on logout
 * CsrfTokenUtils.removeToken(request, response);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 04/01/2026
 * @since 1.0.0
 */
public final class CsrfTokenUtils {

    private CsrfTokenUtils() {
    }

    /**
     * Get existing token or create new one if missing.
     *
     * @param req  HTTP request
     * @param resp HTTP response
     * @return CSRF token
     */
    public static String getOrCreate(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(SecurityKeys.SESSION_CSRF_TOKEN);

        if (token == null || token.isBlank()) {
            // Generate secure token using TokenUtils
            token = TokenUtils.generateToken();

            // Store in session
            session.setAttribute(SecurityKeys.SESSION_CSRF_TOKEN, token);

            // Store in cookie using AppCookie factory (with httpOnly override for JS access)
            AppCookie cookie = AppCookie.strict(
                    SecurityKeys.CSRF_TOKEN_NAME,
                    token,
                    SecurityKeys.CSRF_TOKEN_MAX_AGE
            ).httpOnly(false); // Override: JS needs to read for Unpoly header

            resp.addCookie(cookie);
        }

        return token;
    }

    /**
     * Verify CSRF token using double submit cookie pattern.
     *
     * @param req HTTP request
     * @return true if token is valid
     */
    public static boolean verifyToken(HttpServletRequest req) {
        // Get token from cookie using CookieUtils
        String cookieToken = CookieUtils.get(req, SecurityKeys.CSRF_TOKEN_NAME);
        if (cookieToken == null || cookieToken.isBlank()) {
            return false;
        }

        // Get token from request (header takes precedence over parameter)
        String requestToken = req.getHeader(SecurityKeys.CSRF_HEADER_NAME);
        if (requestToken == null || requestToken.isBlank()) {
            requestToken = req.getParameter(SecurityKeys.CSRF_PARAM_NAME);
        }

        if (requestToken == null || requestToken.isBlank()) {
            return false;
        }

        // Use TokenUtils's constant-time comparison
        return TokenUtils.constantTimeEquals(cookieToken, requestToken);
    }

    /**
     * Remove CSRF token from session and cookie.
     *
     * @param req  HTTP request
     * @param resp HTTP response
     */
    public static void removeToken(HttpServletRequest req, HttpServletResponse resp) {
        // Remove from session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(SecurityKeys.SESSION_CSRF_TOKEN);
        }

        // Clear cookie using CookieUtils
        CookieUtils.clear(SecurityKeys.CSRF_TOKEN_NAME, resp);
    }
}
