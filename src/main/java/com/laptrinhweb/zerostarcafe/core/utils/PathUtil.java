package com.laptrinhweb.zerostarcafe.core.utils;

import com.laptrinhweb.zerostarcafe.web.common.view.ViewArea;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class providing centralized JSP view path construction for all web areas.
 * Works with ViewArea enum to build full JSP paths from relative paths.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * String jspPath = PathUtil.getViewPath(ViewArea.CLIENT, "pages/home");
 * // Returns: "/WEB-INF/views/client/pages/home.jsp"
 * </pre>
 *
 * @author Dang Van Trung
 * @version 2.1.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class PathUtil {

    private PathUtil() {
    }

    /**
     * Build full JSP path from area and relative path.
     * 
     * @param area ViewArea (CLIENT, ADMIN, SHARED)
     * @param relativePath Relative path like "pages/home" or "forms/_login"
     * @return Full JSP path like "/WEB-INF/views/client/pages/home.jsp"
     */
    public static String getViewPath(ViewArea area, String relativePath) {
        String normalized = normalize(relativePath);
        return area.getBasePath() + "/" + normalized + ".jsp";
    }

    private static String normalize(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path must not be blank");
        }
        return path.trim()
                   .replaceFirst("^/", "")
                   .replaceFirst("\\.jsp$", "");
    }

    // ============================
    // Static Path Detection
    // ============================
    private static final String[] STATIC_PREFIXES = {
            "/assets/"
    };

    private static final String[] STATIC_EXTENSIONS = {
            ".css", ".js", ".map",
            ".png", ".jpg", ".jpeg", ".gif", ".svg", ".webp",
            ".ico",
            ".ttf", ".woff", ".woff2",
            ".mp4", ".mp3"
    };

    public static boolean isStatic(String path) {
        if (path == null) return false;

        path = path.toLowerCase();

        for (String pre : STATIC_PREFIXES) {
            if (path.startsWith(pre)) return true;
        }

        for (String ext : STATIC_EXTENSIONS) {
            if (path.endsWith(ext)) return true;
        }

        return false;
    }
}