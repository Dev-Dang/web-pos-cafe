package com.laptrinhweb.zerostarcafe.web.common.view;

import static com.laptrinhweb.zerostarcafe.core.utils.PathUtil.getViewPath;

/**
 * <h2>Description:</h2>
 * <p>
 * Central registry for all application views.
 * Single source of truth - prevents magic strings.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * View.render(ViewMap.Client.HOME, req, resp);
 * View.render(ViewMap.Client.LOGIN_FORM, req, resp);
 * View.render(ViewMap.Admin.DASHBOARD, req, resp);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class ViewMap {

    private ViewMap() {
    }

    // ============================================
    // CLIENT AREA
    // ============================================
    public static final class Client {
        private Client() {}

        // Pages
        public static final View HOME = page("pages/home");
        public static final View CART = page("pages/cart");
        public static final View CHECKOUT = page("pages/checkout");
        public static final View INVOICE = page("pages/invoice");

        // Partials - Pages
        public static final View CART_CONTENT = partial("pages/cart-content");

        // Partials - Forms
        public static final View LOGIN_FORM = partial("forms/_login");
        public static final View REGISTER_FORM = partial("forms/_register");
        public static final View FORGOT_PASSWORD_FORM = partial("forms/_forgot-password");
        public static final View RESET_PASSWORD_FORM = partial("forms/_reset-password");
        public static final View PRODUCT_DETAIL = partial("forms/_product-detail");
        
        // Partials - Fragments
        public static final View CART_SIDEBAR = partial("fragments/cart-sidebar");
        public static final View CART_ITEM = partial("fragments/cart-item");
        public static final View PRODUCT_CATALOG = partial("fragments/product-catalog");

        private static View page(String path) {
            return View.page(
                ViewArea.CLIENT,
                getViewPath(ViewArea.CLIENT, path),
                generateTitleKey(ViewArea.CLIENT, path)
            );
        }

        private static View partial(String path) {
            return View.partial(ViewArea.CLIENT, getViewPath(ViewArea.CLIENT, path)).build();
        }
    }

    // ============================================
    // ADMIN AREA
    // ============================================
    public static final class Admin {
        private Admin() {}

        // Pages
        public static final View DASHBOARD = page("pages/dashboard");

        private static View page(String path) {
            return View.page(
                ViewArea.ADMIN,
                getViewPath(ViewArea.ADMIN, path),
                generateTitleKey(ViewArea.ADMIN, path)
            );
        }

        private static View partial(String path) {
            return View.partial(ViewArea.ADMIN, getViewPath(ViewArea.ADMIN, path)).build();
        }
    }

    // ============================================
    // AUTO TITLE GENERATION
    // ============================================
    /**
     * Auto-generate i18n title key from area and path.
     * 
     * Examples:
     * - CLIENT, "pages/home" → "general.client.home"
     * - CLIENT, "pages/cart" → "general.client.cart"
     * - ADMIN, "pages/dashboard" → "general.admin.dashboard"
     */
    static String generateTitleKey(ViewArea area, String path) {
        String slug = path.replaceFirst("^pages/", "")
                         .replaceAll("/", ".")
                         .replaceAll("_", "");
        return "general." + area.name().toLowerCase() + "." + slug;
    }

    // ============================================
    // SHARED AREA (Error pages, etc.)
    // ============================================
    public static final class Shared {
        private Shared() {}

        public static final View ERROR = View.page(
                ViewArea.SHARED,
                "/WEB-INF/views/shared/pages/error.jsp",
                "general.error"
        );
    }

    // ============================================
    // DEFAULT VIEWS
    // ============================================
    public static View getDefaultFor(ViewArea area) {
        return switch (area) {
            case ADMIN -> Admin.DASHBOARD;
            case CLIENT -> Client.HOME;
            case SHARED -> Shared.ERROR;
        };
    }
    
    // ============================================
    // PARTIAL LOOKUP BY NAME (Convention-based)
    // ============================================
    /**
     * Resolves a partial view by its name using naming convention.
     * Converts kebab-case URL names to SCREAMING_SNAKE_CASE field names.
     * 
     * Examples:
     * - "login-form" → Client.LOGIN_FORM
     * - "cart-sidebar" → Client.CART_SIDEBAR
     * 
     * @param name partial name in kebab-case (e.g., "login-form")
     * @return the View object, or null if not found
     */
    public static View getPartialByName(String name) {
        try {
            // Convert: "login-form" → "LOGIN_FORM"
            String fieldName = name.toUpperCase().replace("-", "_");
            return getStaticFieldValue(Client.class, fieldName);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Gets the value of a static field using reflection.
     * 
     * @param clazz the class containing the field
     * @param fieldName the field name
     * @return the field value as View
     * @throws Exception if field not found or not accessible
     */
    private static View getStaticFieldValue(Class<?> clazz, String fieldName) throws Exception {
        var field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (View) field.get(null);
    }
}