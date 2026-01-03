package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CSRF token manager using Double Submit Cookie pattern.
 * Wraps TokenUtil with CSRF-specific logic for session + cookie management.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
public final class CsrfTokenManager {

    private CsrfTokenManager() {}

    /**
     * Generates and sets CSRF token in both session and cookie.
     * Uses TokenUtil for secure token generation and AppCookie for cookie config.
     *
     * @param req  HTTP request
     * @param resp HTTP response
     * @return the generated token
     */
    public static String generateAndSet(HttpServletRequest req, HttpServletResponse resp) {
        // Generate secure token using TokenUtil
        String token = TokenUtil.generateToken();
        
        // Store in session
        HttpSession session = req.getSession(true);
        session.setAttribute(SecurityKeys.SESSION_CSRF_TOKEN, token);
        
        // Store in cookie using AppCookie factory (with httpOnly override for JS access)
        AppCookie cookie = AppCookie.strict(
            SecurityKeys.CSRF_TOKEN_NAME,
            token,
            SecurityKeys.CSRF_TOKEN_MAX_AGE
        ).httpOnly(false); // Override: JS needs to read for Unpoly header
        
        resp.addCookie(cookie);
        
        return token;
    }

    /**
     * Gets existing CSRF token from session, or generates new one if missing.
     *
     * @param req  HTTP request
     * @param resp HTTP response
     * @return the CSRF token
     */
    public static String getOrCreate(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(SecurityKeys.SESSION_CSRF_TOKEN);
        
        if (token == null || token.isBlank()) {
            token = generateAndSet(req, resp);
        }
        
        return token;
    }

    /**
     * Validates CSRF token using double submit cookie pattern.
     * Checks that cookie token matches either header or parameter token.
     * Uses TokenUtil.constantTimeEquals() to prevent timing attacks.
     *
     * @param req HTTP request
     * @return true if valid, false otherwise
     */
    public static boolean validate(HttpServletRequest req) {
        // Get token from cookie using CookieUtil
        String cookieToken = CookieUtil.get(req, SecurityKeys.CSRF_TOKEN_NAME);
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

        // Use TokenUtil's constant-time comparison
        return TokenUtil.constantTimeEquals(cookieToken, requestToken);
    }

    /**
     * Clears CSRF token from session and cookie.
     *
     * @param req  HTTP request
     * @param resp HTTP response
     */
    public static void clear(HttpServletRequest req, HttpServletResponse resp) {
        // Remove from session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(SecurityKeys.SESSION_CSRF_TOKEN);
        }
        
        // Clear cookie using CookieUtil
        CookieUtil.clear(SecurityKeys.CSRF_TOKEN_NAME, resp);
    }
}
