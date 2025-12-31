package com.laptrinhweb.zerostarcafe.web.common.view;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;

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
        public static class Page {
            public static final View HOME = ClientPage(AppRoute.HOME);
        }

        public static class Form {
            public static final View LOGIN = ClientComp(PathUtil.Client.form("_login"));
        }

        public static class Fragment {
            public static final View FLASH_CONTAINER = ClientComp(PathUtil.Client.fragment("_flash-data"));
        }
    }

    public static class Admin {
        public static class Page {
            public static final View DASHBOARD = AdminPage(AppRoute.DASHBOARD);
        }
    }

    public static View getDefaultFor(ViewArea area) {
        return switch (area) {
            case ADMIN -> Admin.Page.DASHBOARD;
            default -> Client.Page.HOME;
        };
    }

    private static View ClientPage(AppRoute route) {
        return View.getPage(ViewArea.CLIENT, route.getPath());
    }

    private static View ClientComp(String viewPath) {
        return View.getComponent(ViewArea.CLIENT, viewPath);
    }

    private static View AdminPage(AppRoute route) {
        return View.getPage(ViewArea.ADMIN, route.getPath());
    }

    private static View AdminComp(String viewPath) {
        return View.getComponent(ViewArea.ADMIN, viewPath);
    }
}