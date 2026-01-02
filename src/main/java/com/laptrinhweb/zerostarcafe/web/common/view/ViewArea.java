package com.laptrinhweb.zerostarcafe.web.common.view;

import lombok.Getter;

/**
 * Defines the UI areas supported by the application (client, admin, shared).
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/12/2025
 * @since 1.0.0
 */
@Getter
public enum ViewArea {
    CLIENT("/WEB-INF/views/client", "_main-layout"),
    ADMIN("/WEB-INF/views/admin", "admin-layout"),
    SHARED("/WEB-INF/views/shared", "_partial-wrapper");

    private final String basePath;
    private final String layoutPath;

    ViewArea(String basePath, String layoutName) {
        this.basePath = basePath;
        this.layoutPath = basePath + "/layouts/" + layoutName + ".jsp";
    }

    public static ViewArea detectArea(String requestPath) {
        if (requestPath == null || requestPath.isBlank()) {
            return CLIENT;
        }

        String path = requestPath.trim().toLowerCase();

        if (path.startsWith("/admin")) {
            return ADMIN;
        }

        return CLIENT;
    }
}