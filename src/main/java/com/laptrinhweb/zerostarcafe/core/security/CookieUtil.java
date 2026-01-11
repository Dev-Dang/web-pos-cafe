package com.laptrinhweb.zerostarcafe.core.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility for retrieving and clearing HTTP Cookies.
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 12/12/2025
 * @since 1.0.0
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    /**
     * Gets the value of a cookie from the {@link HttpServletRequest}.
     *
     * @param request the current {@link HttpServletRequest}
     * @param name    the name of the cookie to get
     * @return the cookie value, or {@code null} if it does not exist
     */
    public static String get(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || name == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                String value = c.getValue();
                if (value == null || value.isBlank()) {
                    return null;
                }
                return value;
            }
        }
        return null;
    }

    /**
     * Returns all cookies from the request as an immutable Map.
     *
     * @param request the current HttpServletRequest
     * @return map of cookie name -> cookie value (never null, may be empty)
     */
    public static Map<String, String> getAll(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        if (request == null || request.getCookies() == null) {
            return map;
        }

        for (Cookie c : request.getCookies()) {
            String name = c.getName();
            String value = c.getValue();

            if (name != null && value != null && !value.isBlank()) {
                map.put(name, value);
            }
        }

        return Map.copyOf(map);
    }

    /**
     * Clears a cookie by overwriting it with empty value
     * and {@code maxAge = 0} (expires immediately)
     *
     * @param name     the name of the cookie to clear
     * @param response the {@link HttpServletResponse} to add the "clearing" cookie to
     */
    public static void clear(String name, HttpServletResponse response) {
        if (name == null || response == null) {
            return;
        }

        Cookie cookie = AppCookie.strict(name, "", 0);
        response.addCookie(cookie);
    }
}