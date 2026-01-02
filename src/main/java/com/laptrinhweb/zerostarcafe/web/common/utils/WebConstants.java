package com.laptrinhweb.zerostarcafe.web.common.utils;

/**
 * Web layer constants for HTTP headers and request attributes.
 * Organized by category with consistent naming patterns.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public final class WebConstants {

    private WebConstants() {
    }

    // ============================================
    // HTTP HEADERS (Unpoly Protocol)
    // ============================================
    public static final class Header {
        private Header() {}

        public static final String UP_VERSION = "X-Up-Version";
        public static final String UP_TARGET = "X-Up-Target";
        public static final String UP_MODE = "X-Up-Mode";
        public static final String UP_ACCEPT_LOCATION = "X-Up-Accept-Location";
    }
}
