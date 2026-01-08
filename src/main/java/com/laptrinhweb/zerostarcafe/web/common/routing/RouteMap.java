package com.laptrinhweb.zerostarcafe.web.common.routing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Central registry for all application routes.
 * Similar to ViewMap - single source of truth for route paths.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RouteMap {

    // ============================================
    // CLIENT ROUTES
    // ============================================
    public static final String HOME = "/home";
    public static final String LOGIN = "/auth/login";
    public static final String REGISTER = "/auth/register";
    public static final String FORGOT_PASSWORD = "/auth/forgot-password";
    public static final String RESET_PASSWORD = "/auth/reset-password";
    public static final String CART = "/cart";
    public static final String CART_ADD = "/cart/add";
    public static final String CART_PREVIEW = "/cart/preview";
    public static final String CART_MERGE = "/cart/merge";
    public static final String CART_SIDEBAR = "/cart/sidebar";
    public static final String CHECKOUT = "/checkout";

    // ============================================
    // API ROUTES
    // ============================================
    public static final String CATEGORY_PRODUCTS = "/categories";
    public static final String PRODUCTS = "/products";
    public static final String SEARCH = "/search";

    // ============================================
    // ADMIN ROUTES
    // ============================================
    public static final String DASHBOARD = "/admin/dashboard";
}
