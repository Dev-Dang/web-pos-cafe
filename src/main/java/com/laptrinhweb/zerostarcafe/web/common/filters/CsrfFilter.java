//package com.laptrinhweb.zerostarcafe.web.common.filters;
//
//import com.laptrinhweb.zerostarcafe.core.security.CsrfTokenUtils;
//import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
//import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
//import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
/// // **
/// / * <h2>Description:</h2>
/// / * <p>
/// / * CSRF protection filter using Double Submit Cookie pattern.
/// / * Validates CSRF tokens for all state-changing requests.
/// / * </p>
/// / *
/// / * @author Dang Van Trung
/// / * @version 1.0.0
/// / * @lastModified 03/01/2026
/// / * @since 1.0.0
/// / */
//@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
//public class CsrfFilter implements Filter {
//
//    private static final Set<String> SAFE_METHODS = new HashSet<>(
//            Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE")
//    );
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) resp;
//
//        String method = request.getMethod();
//
//        // Skip static resources
//        if (RequestUtils.isStaticRequest(request)) {
//            chain.doFilter(req, resp);
//            return;
//        }
//
//        // Skip safe methods (GET, HEAD, OPTIONS, TRACE)
//        if (SAFE_METHODS.contains(method)) {
//            // Get or create CSRF token for safe methods (persisted to session)
//            CsrfTokenUtils.getOrCreate(request, response);
//            chain.doFilter(req, resp);
//            return;
//        }
//
//        // Skip exempted URLs
//        if (isExempted(request)) {
//            chain.doFilter(req, resp);
//            return;
//        }
//
//        // Validate CSRF token for state-changing methods
//        if (!CsrfTokenUtils.verifyToken(request)) {
//            handleCsrfFailure(request, response);
//            return;
//        }
//
//        // Valid token - continue
//        chain.doFilter(req, resp);
//    }
//
//    /**
//     * Check if request is exempted from CSRF validation.
//     */
//    private boolean isExempted(HttpServletRequest req) {
//        String uri = req.getRequestURI();
//        String path = uri.substring(req.getContextPath().length());
//
//        // Add exemptions here (e.g., errors, public APIs)
//        return path.startsWith("/error");
//    }
//
//    /**
//     * Handle CSRF validation failure using AppRoute for centralized error handling.
//     */
//    private void handleCsrfFailure(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        String uri = req.getRequestURI();
//        String path = uri.substring(req.getContextPath().length());
//
//        LoggerUtil.warn(getClass(),
//                String.format("CSRF validation failed - Method: %s, URI: %s, IP: %s",
//                        req.getMethod(), path, req.getRemoteAddr()));
//
//        // Use AppRoute for centralized error handling
//        AppRoute.sendError(
//                HttpServletResponse.SC_FORBIDDEN,
//                "CSRF token validation failed. Please refresh the page and try again.",
//                resp
//        );
//    }
//}
