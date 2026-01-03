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
        private Header() {
        }

        public static final String UP_VERSION = "X-Up-Version";
        public static final String UP_TARGET = "X-Up-Target";
        public static final String UP_MODE = "X-Up-Mode";
        public static final String UP_LOCATION = "X-Up-Location";
    }

    // ============================================
    // LOCALE & INTERNATIONALIZATION
    // ============================================
    public static final class Locale {
        private Locale() {
        }

        public static final String COOKIE_NAME = "lang";
        public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30; // 30 days
        public static final String SESSION_ATTRIBUTE = "locale";
        public static final String I18N_ATTRIBUTE = "i18n";
        public static final String SUPPORTED_VI = "vi-VN";
        public static final String SUPPORTED_EN = "en-US";
        public static final String DEFAULT_LOCALE = "vi-VN";
    }
}
