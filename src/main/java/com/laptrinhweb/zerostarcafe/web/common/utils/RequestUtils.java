package com.laptrinhweb.zerostarcafe.web.common.utils;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Utilities for HTTP request and response handling. Provides methods for detecting
 * partial requests, static resources, and extracting path parameters safely.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Check request types
 * boolean isPartial = RequestUtils.isPartialRequest(request);
 *
 * // Extract path parameters (returns null on error)
 * Long productId = RequestUtils.extractLongParam(request, "productId");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 04/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtils {
    
    /**
     * Check if request is partial (e.g., from Unpoly).
     *
     * @param req HTTP request
     * @return true if partial request
     */
    public static boolean isPartialRequest(HttpServletRequest req) {
        if (req == null) return false;
        return req.getHeader(WebConstants.Header.UP_VERSION) != null;
    }

    /**
     * Check if request is for static resources.
     *
     * @param req HTTP request
     * @return true if static resource request
     */
    public static boolean isStaticRequest(HttpServletRequest req) {
        if (req == null) return false;
        String uri = req.getRequestURI();
        String path = uri.substring(req.getContextPath().length());
        return PathUtil.isStatic(path);
    }

    /**
     * Extract path parameter as Long, returns null on error.
     *
     * @param req       HTTP request
     * @param paramName parameter name for logging
     * @return Long value or null if invalid/missing
     */
    public static Long extractLongParam(HttpServletRequest req, String paramName) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                return null;
            }
            String param = pathInfo.replace("/", "").trim();
            if (param.isEmpty()) {
                return null;
            }
            return Long.parseLong(param);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract path parameter as String, returns null on error.
     *
     * @param req       HTTP request
     * @param paramName parameter name for logging
     * @return String value or null if missing
     */
    public static String extractStringParam(HttpServletRequest req, String paramName) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                return null;
            }
            String param = pathInfo.replace("/", "").trim();
            if (param.isEmpty()) {
                return null;
            }
            return param;
        } catch (Exception e) {
            return null;
        }
    }
}