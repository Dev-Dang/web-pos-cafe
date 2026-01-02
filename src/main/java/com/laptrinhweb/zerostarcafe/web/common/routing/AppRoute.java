package com.laptrinhweb.zerostarcafe.web.common.routing;

import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.utils.WebConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Central routing utility with smart redirect handling.
 * Automatically detects request type (partial vs normal) and applies the correct redirect strategy.
 * 
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Smart redirect - automatically handles partial vs normal requests
 * AppRoute.redirect(RouteMap.HOME, req, resp);
 *
 * // Send 404 error
 * AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND, resp);
 * 
 * // Get full URL
 * String url = AppRoute.getUrl(RouteMap.HOME, req);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 3.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class AppRoute {

    private AppRoute() {}

    /**
     * Smart redirect that automatically detects request type.
     * - If partial request: sends X-Up-Accept-Location header (client-side navigation)
     * - If normal request: sends HTTP 302 redirect
     *
     * @param path the target route path from RouteMap
     * @param req  the HTTP request
     * @param resp the HTTP response
     * @throws IOException if an I/O error occurs
     */
    public static void redirect(String path, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String targetUrl = getUrl(path, req);
        
        if (RequestUtils.isPartialRequest(req)) {
            // Partial request - use X-Up-Accept-Location header for client-side navigation
            resp.setHeader(WebConstants.Header.UP_ACCEPT_LOCATION, targetUrl);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("");
        } else {
            // Normal request - use HTTP redirect
            resp.sendRedirect(targetUrl);
        }
    }

    /**
     * Sends an HTTP error response with the given status code.
     *
     * @param status the HTTP status code
     * @param resp   the HTTP response
     * @throws IOException if an I/O error occurs
     */
    public static void sendError(int status, HttpServletResponse resp)
            throws IOException {
        resp.sendError(status);
    }

    /**
     * Sends an HTTP error response with a status code and message.
     *
     * @param status  the HTTP status code
     * @param message the error message to send (can be null)
     * @param resp    the HTTP response
     * @throws IOException if an I/O error occurs
     */
    public static void sendError(int status, String message, HttpServletResponse resp)
            throws IOException {
        if (message != null) {
            resp.sendError(status, message);
        } else {
            resp.sendError(status);
        }
    }

    /**
     * Builds the full URL for the given route using the request context path.
     *
     * @param path the route path from RouteMap
     * @param req the HTTP request
     * @return the full URL string
     */
    public static String getUrl(String path, HttpServletRequest req) {
        return req.getContextPath() + path;
    }
}
