package com.laptrinhweb.zerostarcafe.web.common.view;

import static com.laptrinhweb.zerostarcafe.core.utils.PathUtil.getViewPath;

/**
 * <h2>Description:</h2>
 * <p>
 * Resolves URL paths to {@link View} objects for dynamic routing.
 * Used by BaseServlet for convention-based view resolution.
 * Most views should use ViewMap constants for better type safety.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ViewArea area = ViewArea.detectArea("/admin/users");
 * View view    = ViewResolver.resolve(area, "/admin/users");
 * // Produces:
 * //   -> area: ADMIN
 * //   -> viewPath:  /WEB-INF/views/admin/pages/users.jsp
 * //   -> titleKey: general.admin.users
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class ViewResolver {

    private ViewResolver() {
    }

    /**
     * Resolves a request path into a {@link View} for the given UI area.
     *
     * @param area UI area (client/admin/shared)
     * @param path request path from the browser
     * @return resolved {@link View}, or default view if resolution fails
     */
    public static View resolve(ViewArea area, String path) {
        if (area == null || path == null)
            return ViewMap.getDefaultFor(area);

        // Strip area prefix from path
        String areaPrefix = area != ViewArea.CLIENT
                ? "/" + area.name().toLowerCase()
                : "";

        String viewPath = path
                .replaceFirst("^" + areaPrefix + "(?=/|$)", "")
                .replaceFirst("^/", "");

        // Return default if this view path is blank
        if (viewPath.isBlank())
            return ViewMap.getDefaultFor(area);

        // Auto-generate title and JSP path
        String titleKey = ViewMap.generateTitleKey(area, "pages/" + viewPath);
        String jspPath = getViewPath(area, "pages/" + viewPath);

        return View.page(area, jspPath, titleKey);
    }
}