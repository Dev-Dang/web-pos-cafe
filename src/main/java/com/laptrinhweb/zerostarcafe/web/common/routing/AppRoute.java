package com.laptrinhweb.zerostarcafe.web.common.routing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Central place to manage common application routes.
 * Provides helper methods to build URLs, redirect users,
 * and send HTTP error responses.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Redirect to home
 * AppRoute.HOME.redirect(req, resp);
 *
 * // Send 404 error
 * AppRoute.error(HttpServletResponse.SC_NOT_FOUND, resp);
 *
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
public enum AppRoute {

    // Client Routes
    HOME("/home"),
    LOGIN("/auth/login"),

    // Admin Routes
    DASHBOARD("/admin/dashboard");

    private final String path;

    AppRoute(String route) {
        this.path = route;
    }

    /**
     * Redirects to this route using the current context path.
     *
     * @param req  the HTTP request
     * @param resp the HTTP response
     * @throws IOException if an I/O error occurs
     */
    public void redirect(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + this.path);
    }

    /**
     * Sends an HTTP error response with the given status code.
     *
     * @param status the HTTP status code, for example: HttpServletResponse.SC_NOT_FOUND
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
     * @param status  the HTTP status code, for example: HttpServletResponse.SC_FORBIDDEN
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
     * Builds the full URL for this route using the request context path.
     *
     * @param req the HTTP request
     * @return the full URL string for this route
     */
    public String getUrl(HttpServletRequest req) {
        return req.getContextPath() + this.path;
    }

    public String getPath() {
        return this.path;
    }
}