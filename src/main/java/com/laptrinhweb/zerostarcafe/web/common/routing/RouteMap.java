package com.laptrinhweb.zerostarcafe.web.common.routing;

/**
 * Central registry for all application routes.
 * Similar to ViewMap - single source of truth for route paths.
 * 
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Forward to home
 * AppRoute.forward(RouteMap.HOME, req, resp);
 * 
 * // Redirect to login
 * AppRoute.redirect(RouteMap.LOGIN, req, resp);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class RouteMap {
    
    private RouteMap() {}
    
    // ============================================
    // CLIENT ROUTES
    // ============================================
    public static final String HOME = "/home";
    public static final String LOGIN = "/auth/login";
    public static final String REGISTER = "/auth/register";
    public static final String FORGOT_PASSWORD = "/auth/forgot-password";
    public static final String RESET_PASSWORD = "/auth/reset-password";
    public static final String CART = "/cart";
    public static final String CHECKOUT = "/checkout";
    
    // ============================================
    // ADMIN ROUTES
    // ============================================
    public static final String DASHBOARD = "/admin/dashboard";
}
