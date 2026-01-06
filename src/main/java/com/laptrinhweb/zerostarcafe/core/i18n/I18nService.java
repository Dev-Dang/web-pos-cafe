package com.laptrinhweb.zerostarcafe.core.i18n;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <h2>Description:</h2>
 * <p>
 * Thread-safe singleton service for internationalization using ResourceBundle.
 * Leverages Java's built-in ResourceBundle caching for optimal performance.
 * Stateless design using LocaleContext for current request locale.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public final class I18nService {

    private static final I18nService INSTANCE = new I18nService();
    private static final List<String> SUPPORTED_BUNDLES = List.of("form", "message", "general");
    private static final String BUNDLE_PATH_TEMPLATE = "translate/%s/%s";

    private I18nService() {
        // Private constructor for singleton
    }

    /**
     * Gets the singleton instance of I18nService.
     *
     * @return the I18nService instance
     */
    public static I18nService getInstance() {
        return INSTANCE;
    }

    /**
     * Translates key using current request locale from LocaleContext.
     *
     * @param key the translation key (format: "bundleName.keyName")
     * @return translated text or key if not found
     */
    public String trans(String key) {
        return trans(key, LocaleContext.getLocale());
    }

    /**
     * Translates key using specific locale.
     *
     * @param key    the translation key (format: "bundleName.keyName")
     * @param locale the target locale
     * @return translated text or key if not found
     */
    public String trans(String key, Locale locale) {
        if (key == null || key.isBlank()) {
            return "";
        }

        int dotIndex = key.indexOf('.');
        if (dotIndex == -1 || dotIndex == 0 || dotIndex == key.length() - 1) {
            return key;
        }

        String bundleName = key.substring(0, dotIndex);

        // Validate bundle name
        if (!SUPPORTED_BUNDLES.contains(bundleName)) {
            LoggerUtil.warn(I18nService.class, "Unsupported bundle: " + bundleName);
            return key;
        }

        try {
            String bundlePath = String.format
                    (BUNDLE_PATH_TEMPLATE, locale.toLanguageTag(), bundleName);
            ResourceBundle bundle = ResourceBundle.getBundle(bundlePath, locale);

            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException e) {
            LoggerUtil.warn(I18nService.class, "Missing resource: " + e.getMessage());
        }

        return key;
    }

    /**
     * Checks if a translation key exists for the current locale.
     *
     * @param key the translation key to check
     * @return true if key exists, false otherwise
     */
    public boolean hasKey(String key) {
        return hasKey(key, LocaleContext.getLocale());
    }

    /**
     * Checks if a translation key exists for the specified locale.
     *
     * @param key    the translation key to check
     * @param locale the target locale
     * @return true if key exists, false otherwise
     */
    public boolean hasKey(String key, Locale locale) {
        if (key == null || key.isBlank()) {
            return false;
        }

        int dotIndex = key.indexOf('.');
        if (dotIndex == -1 || dotIndex == 0 || dotIndex == key.length() - 1) {
            return false;
        }

        String bundleName = key.substring(0, dotIndex);

        if (!SUPPORTED_BUNDLES.contains(bundleName)) {
            return false;
        }

        try {
            String bundlePath = String.format(BUNDLE_PATH_TEMPLATE, locale.toLanguageTag(), bundleName);
            ResourceBundle bundle = ResourceBundle.getBundle(bundlePath, locale);
            return bundle.containsKey(key);
        } catch (MissingResourceException e) {
            return false;
        }
    }

    /**
     * Gets current locale from LocaleContext.
     *
     * @return current request locale
     */
    public Locale getCurrentLocale() {
        return LocaleContext.getLocale();
    }

    /**
     * Gets current language code from LocaleContext.
     *
     * @return current language code (e.g., "vi", "en")
     */
    public String getCurrentLanguage() {
        return LocaleContext.getLanguage();
    }
}