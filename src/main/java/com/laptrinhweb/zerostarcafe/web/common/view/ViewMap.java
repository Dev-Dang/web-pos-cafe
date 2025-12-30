package com.laptrinhweb.zerostarcafe.web.common.view;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides predefined {@link View} instances for commonly used pages.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ViewMap.Client.HOME:
 *   viewPath = "/home"
 *   resolves to /WEB-INF/views/client/pages/home.jsp
 *
 * ViewMap.Admin.DASHBOARD:
 *   viewPath = "/dashboard"
 *   resolves to /WEB-INF/views/admin/pages/dashboard.jsp
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/12/2025
 * @since 1.0.0
 */
public final class ViewMap {

    private ViewMap() {
    }

    public static class Client {
        public static final View HOME = View.client("/home");
        public static final View CHECKOUT = View.client("/checkout");
        public static final View INVOICE = View.client("/invoice");
    }

    public static class Admin {
        public static final View DASHBOARD = View.admin("/admin/dashboard");
    }

    public static View getDefaultFor(ViewArea area) {
        return switch (area) {
            case ADMIN -> Admin.DASHBOARD;
            default -> Client.HOME;
        };
    }
}