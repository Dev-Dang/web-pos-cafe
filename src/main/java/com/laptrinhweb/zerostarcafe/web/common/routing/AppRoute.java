package com.laptrinhweb.zerostarcafe.web.common.routing;

import com.laptrinhweb.zerostarcafe.web.common.response.RespContext;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.utils.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Central routing utility with smart redirect handling.
 * Automatically detects request type (partial vs normal) and applies the correct redirect strategy.
 * </p>
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
 * @version 2.0.0
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppRoute {

    /**
     * Smart redirect that automatically detects request type.
     * For Unpoly redirects, renders response context JSP with flash messages.
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
            resp.setHeader(WebConstants.Header.UP_LOCATION, targetUrl);
            resp.setStatus(HttpServletResponse.SC_SEE_OTHER);

            // Render response context if there are messages
            if (RespContext.from(req).hasData()) {
                try {
                    AppRoute.forward(ViewMap.Shared.RESPONSE_CONTEXT, req, resp);
                } catch (Exception e) {
                    // Silently fail - redirect still works
                }
            }
        } else {
            resp.sendRedirect(targetUrl);
        }
    }

    public static void forward(String path, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
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
     * @param req  the HTTP request
     * @return the full URL string
     */
    public static String getUrl(String path, HttpServletRequest req) {
        return req.getContextPath() + path;
    }
}
