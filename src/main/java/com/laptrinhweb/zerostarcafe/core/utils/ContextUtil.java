package com.laptrinhweb.zerostarcafe.core.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility for retrieving required objects from {@link ServletContext}.
 * Throws {@link ServletException} when a dependency is missing.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public final class ContextUtil {

    private ContextUtil() {
    }

    public static <T> T require(ServletContext ctx, String key, Class<T> type)
            throws ServletException {

        Object obj = ctx.getAttribute(key);

        if (obj == null) {
            LoggerUtil.warn(ContextUtil.class,
                    "Missing dependency in ServletContext: " + key);
            throw new ServletException(
                    "Missing dependency in ServletContext: " + key
            );
        }

        return type.cast(obj);
    }
}
