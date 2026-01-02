package com.laptrinhweb.zerostarcafe.web.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utilities for HTTP request and response handling.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class RequestUtils {

    private RequestUtils() {}

    /**
     * Check if request is a partial request (e.g., from Unpoly).
     * Detects X-Up-Version header sent by partial request frameworks.
     *
     * @param req HTTP request
     * @return true if this is a partial request
     */
    public static boolean isPartialRequest(HttpServletRequest req) {
        if (req == null) return false;
        return req.getHeader(WebConstants.Header.UP_VERSION) != null;
    }

    /**
     * Extracts a single path parameter and converts it to Long.
     * Use when URL pattern is /resource/{id}
     *
     * @param req       the HTTP request
     * @param paramName descriptive name for error messages
     * @return the extracted Long value
     * @throws IllegalArgumentException if parameter is missing or invalid
     */
    public static Long extractLongParam(HttpServletRequest req, String paramName) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        String param = pathInfo.replace("/", "").trim();
        if (param.isEmpty()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + paramName + ": " + param, e);
        }
    }

    /**
     * Extracts a single path parameter as String.
     * Use when parameter is not numeric (e.g., slug).
     *
     * @param req       the HTTP request
     * @param paramName descriptive name for error messages
     * @return the extracted String value
     * @throws IllegalArgumentException if parameter is missing
     */
    public static String extractStringParam(HttpServletRequest req, String paramName) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        String param = pathInfo.replace("/", "").trim();
        if (param.isEmpty()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        return param;
    }
}
