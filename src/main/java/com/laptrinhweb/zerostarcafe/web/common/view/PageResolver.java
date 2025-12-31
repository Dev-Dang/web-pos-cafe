package com.laptrinhweb.zerostarcafe.web.common.view;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;

/**
 * <h2>Description:</h2>
 * <p>
 * Resolves friendly URL paths into {@link View} objects used for rendering JSP pages.
 * Handles normalization, default views, and error view generation.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ViewArea area = ViewArea.detectArea("/admin/users");
 * View view    = PageResolver.resolve(area, "/admin/users");
 * // Produces:
 * //   -> area: ADMIN
 * //   -> pagePath:  /WEB-INF/views/admin/pages/users.jsp
 * //   -> layoutPath: /WEB-INF/views/admin/layouts/_main-layout.jsp
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 10/12/2025
 * @since 1.0.0
 */
public final class PageResolver {

    private static final String PAGE_TITLE_PREFIX = "general.";
    private static final String ERROR_VIEW_PATH = PathUtil.Shared.page("error");
    private static final String ERROR_PAGE_TITLE = "general.page.error";

    private PageResolver() {
    }

    /**
     * Resolves a request path (or logical view path) into a {@link View} for the given UI area.
     *
     * @param area UI area (client/admin)
     * @param path request path from the browser
     * @return resolved {@link View}, or an error view if resolution fails
     */
    public static View resolve(ViewArea area, String path) {
        if (area == null || path == null)
            return resolveError(ViewArea.CLIENT);

        // Normalize view path
        String areaName = area != ViewArea.CLIENT
                ? area.name().toLowerCase()
                : "";
        String viewPath = path.
                replaceFirst("^/" + areaName + "(?=/|$)", "");

        // Return default if this view path is blank
        if (viewPath.isBlank())
            return ViewMap.getDefaultFor(area);

        return new View(
                area,
                View.Type.PAGE,
                resolvePageTitle(area, viewPath),
                resolvePagePath(area, viewPath),
                resolveLayoutPath(area)
        );
    }

    /**
     * Returns a standardized error {@link View} for the given UI area.
     *
     * @param area the UI area whose layout should be used
     * @return a {@link View} representing the error page
     */
    public static View resolveError(ViewArea area) {
        return new View(
                area,
                View.Type.PAGE,
                ERROR_PAGE_TITLE,
                ERROR_VIEW_PATH,
                resolveLayoutPath(area)
        );
    }

    private static String resolvePageTitle(ViewArea area, String viewPath) {
        String normalized = viewPath.replaceFirst("^/", "");
        String slug = normalized.replaceAll("/", ".");
        return PAGE_TITLE_PREFIX + area.name().toLowerCase() + "." + slug;
    }

    private static String resolvePagePath(ViewArea area, String viewPath) {
        String normalized = viewPath.replaceFirst("^/", "");
        return switch (area) {
            case ADMIN -> PathUtil.Admin.page(normalized);
            default -> PathUtil.Client.page(normalized);
        };
    }

    private static String resolveLayoutPath(ViewArea area) {
        return switch (area) {
            case ADMIN -> PathUtil.Admin.layoutMain();
            default -> PathUtil.Client.layoutMain();
        };
    }
}