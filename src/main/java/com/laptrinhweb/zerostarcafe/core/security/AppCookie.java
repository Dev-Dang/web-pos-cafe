package com.laptrinhweb.zerostarcafe.core.security;

import jakarta.servlet.http.Cookie;

/**
 * <h2>Description:</h2>
 * <p>
 * A helper cookie class that allows method chaining. It makes it easy
 * to set common cookie options such as path, httpOnly, secure, sameSite,
 * and maxAge when working with servlets.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AppCookie cookie = AppCookie.strict("token", "abc", 3600);
 * resp.addCookie(cookie);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
public class AppCookie extends Cookie {

    public enum SameSite {STRICT, LAX, NONE}

    public AppCookie(String name, String value) {
        super(name, value);
    }

    /**
     * Creates a secure cookie for sensitive data.
     *
     * @param name   cookie name
     * @param value  cookie value
     * @param maxAge lifetime in seconds
     * @return a configured secure AppCookie
     */
    public static AppCookie strict(String name, String value, int maxAge) {
        return new AppCookie(name, value)
                .path("/")
                .httpOnly(true)             // JS cannot read
                .secure(true)               // HTTPS only
                .sameSite(SameSite.STRICT)  // No cross-site sending
                .maxAge(maxAge);
    }

    /**
     * Creates a cookie that JavaScript can read.
     * Good for non-sensitive data used by the UI.
     *
     * @param name   cookie name
     * @param value  cookie value
     * @param maxAge lifetime in seconds
     * @return a configured accessible AppCookie
     */
    public static AppCookie accessible(String name, String value, int maxAge) {
        return new AppCookie(name, value)
                .path("/")
                .httpOnly(false)            // JS CAN read
                .secure(true)               // HTTPS only
                .sameSite(SameSite.LAX)     // Better UX for navigation
                .maxAge(maxAge);
    }

    public AppCookie path(String path) {
        super.setPath(path);
        return this;
    }

    public AppCookie httpOnly(boolean httpOnly) {
        super.setHttpOnly(httpOnly);
        return this;
    }

    public AppCookie secure(boolean secure) {
        super.setSecure(secure);
        return this;
    }

    public AppCookie sameSite(SameSite sameSite) {
        String value = switch (sameSite) {
            case STRICT -> "Strict";
            case LAX -> "Lax";
            case NONE -> "None";
        };
        super.setAttribute("SameSite", value);
        return this;
    }

    public AppCookie maxAge(int maxAge) {
        super.setMaxAge(maxAge);
        return this;
    }
}
