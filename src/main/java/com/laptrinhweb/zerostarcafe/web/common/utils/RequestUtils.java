package com.laptrinhweb.zerostarcafe.web.common.utils;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.LoyaltyPointsDTO;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Utilities for HTTP request and response handling. Provides methods for detecting
 * partial requests, static resources, extracting parameters and path parameters safely.
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 05/01/2026
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

    // ==========================================================
    // QUERY PARAMETER UTILITIES
    // ==========================================================

    /**
     * Get query parameter as String with default value.
     *
     * @param req          HTTP request
     * @param paramName    parameter name
     * @param defaultValue default value if parameter is missing or empty
     * @return parameter value or default
     */
    public static String getStringParam(HttpServletRequest req, String paramName, String defaultValue) {
        if (req == null || paramName == null)
            return defaultValue;
        String value = req.getParameter(paramName);
        return (value != null && !value.trim().isEmpty())
                ? value.trim()
                : defaultValue;
    }

    /**
     * Get query parameter as String, returns null if missing.
     *
     * @param req       HTTP request
     * @param paramName parameter name
     * @return parameter value or null
     */
    public static String getStringParam(HttpServletRequest req, String paramName) {
        return getStringParam(req, paramName, null);
    }

    /**
     * Get query parameter as Long with default value.
     *
     * @param req          HTTP request
     * @param paramName    parameter name
     * @param defaultValue default value if parameter is invalid or missing
     * @return parameter value or default
     */
    public static Long getLongParam(HttpServletRequest req, String paramName, Long defaultValue) {
        try {
            String value = getStringParam(req, paramName);
            return value != null ? Long.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get query parameter as Long, returns null if invalid.
     *
     * @param req       HTTP request
     * @param paramName parameter name
     * @return parameter value or null
     */
    public static Long getLongParam(HttpServletRequest req, String paramName) {
        return getLongParam(req, paramName, null);
    }

    /**
     * Get query parameter as Integer with default value.
     *
     * @param req          HTTP request
     * @param paramName    parameter name
     * @param defaultValue default value if parameter is invalid or missing (can be null)
     * @return parameter value or default
     */
    public static Integer getIntParam(HttpServletRequest req, String paramName, Integer defaultValue) {
        try {
            String value = getStringParam(req, paramName);
            return value != null ? Integer.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get query parameter as int with default value.
     *
     * @param req       HTTP request
     * @param paramName parameter name
     * @return parameter value or default
     */
    public static Integer getIntParam(HttpServletRequest req, String paramName) {
        return getIntParam(req, paramName, null);
    }

    /**
     * Get query parameter as Double with default value.
     *
     * @param req          HTTP request
     * @param paramName    parameter name
     * @param defaultValue default value if parameter is invalid or missing
     * @return parameter value or default
     */
    public static Double getDoubleParam(HttpServletRequest req, String paramName, Double defaultValue) {
        try {
            String value = getStringParam(req, paramName);
            return value != null ? Double.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get query parameter as Double, returns null if invalid.
     *
     * @param req       HTTP request
     * @param paramName parameter name
     * @return parameter value or null
     */
    public static Double getDoubleParam(HttpServletRequest req, String paramName) {
        return getDoubleParam(req, paramName, null);
    }

    // ==========================================================
    // PATH PARAMETER UTILITIES
    // ==========================================================

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

    /**
     * Extract path parameter as Integer, returns null on error.
     *
     * @param req       HTTP request
     * @param paramName parameter name for logging
     * @return Integer value or null if invalid/missing
     */
    public static Integer extractIntegerParam(HttpServletRequest req, String paramName) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                return null;
            }
            String param = pathInfo.replace("/", "").trim();
            if (param.isEmpty()) {
                return null;
            }
            return Integer.parseInt(param);
        } catch (Exception e) {
            return null;
        }
    }

    // ==========================================================
    // VALIDATION UTILITIES
    // ==========================================================

    /**
     * Check if required parameter is present and not empty.
     *
     * @param req       HTTP request
     * @param paramName parameter name
     * @return true if parameter exists and not empty
     */
    public static boolean hasRequiredParam(HttpServletRequest req, String paramName) {
        String value = getStringParam(req, paramName);
        return value != null && !value.isEmpty();
    }

    /**
     * Check if any of the required parameters are missing.
     *
     * @param req        HTTP request
     * @param paramNames parameter names to check
     * @return true if all required parameters are present
     */
    public static boolean hasAllRequiredParams(HttpServletRequest req, String... paramNames) {
        if (paramNames == null || paramNames.length == 0) return true;

        for (String paramName : paramNames) {
            if (!hasRequiredParam(req, paramName)) {
                return false;
            }
        }
        return true;
    }

    // ==========================================================
    // SESSION UTILITIES
    // ==========================================================

    /**
     * Get store ID from session context with fallback to default.
     *
     * @param req HTTP request
     * @return store ID from session or default store ID
     */
    public static Long getStoreIdFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        StoreContext storeCtx = (StoreContext)
                session.getAttribute(StoreConstants.Attribute.CURRENT_STORE_CTX);
        return storeCtx != null
                ? storeCtx.getStoreId()
                : StoreConstants.DEFAULT_STORE_ID;
    }

    /**
     * Get user ID from authenticated session.
     *
     * @param req HTTP request
     * @return user ID or null if not authenticated
     */
    public static Long getUserIdFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }

        AuthUser authUser = (AuthUser) session
                .getAttribute(SecurityKeys.SESSION_AUTH_USER);

        return authUser != null ? authUser.getId() : null;
    }

    /**
     * Get loyalty points DTO from session.
     *
     * @param req HTTP request
     * @return LoyaltyPointsDTO or null if not found
     */
    public static LoyaltyPointsDTO getLoyaltyPointsFromSession(HttpServletRequest req) {
        if (req == null) return null;
        HttpSession session = req.getSession(false);
        if (session == null) return null;

        return (LoyaltyPointsDTO)
                session.getAttribute(WebConstants.Loyalty.POINTS);
    }

    /**
     * Get apply loyalty points state from session.
     *
     * @param req HTTP request
     * @return true if points should be applied, false otherwise
     */
    public static boolean getApplyLoyaltyFromSession(HttpServletRequest req) {
        if (req == null) return false;
        HttpSession session = req.getSession(false);
        if (session == null) return false;

        Boolean apply = (Boolean) session.getAttribute(WebConstants.Loyalty.APPLY_POINTS);
        return apply != null && apply;
    }
}