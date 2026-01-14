package com.laptrinhweb.zerostarcafe.core.context;

import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * <h2>Description:</h2>
 * <p>
 * Thread-local context holder for the current request's locale information.
 * Provides global access to locale/language without passing parameters through layers.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // In DAO/Service: Access locale anywhere
 * String language = LocaleContext.getLanguage(); // "vi" or "en"
 * Locale locale = LocaleContext.getLocale();     // Full locale object
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocaleContext {

    private static final ThreadLocal<Locale> localeHolder = new ThreadLocal<>();
    private static final Locale DEFAULT_LOCALE = Locale
            .forLanguageTag(WebConstants.Locale.DEFAULT_LOCALE);

    /**
     * Sets the current locale for this request thread.
     * Should be called once per request in a filter.
     *
     * @param locale the locale to set (must not be null)
     * @throws IllegalArgumentException if locale is null
     */
    public static void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        localeHolder.set(locale);
    }

    /**
     * Gets the current locale for this request thread.
     * Returns default locale if none has been set.
     *
     * @return the current locale, never null
     */
    public static Locale getLocale() {
        Locale locale = localeHolder.get();
        return locale != null ? locale : DEFAULT_LOCALE;
    }

    /**
     * Gets the current language code (ISO 639-1) for this request thread.
     * Commonly used for i18n database queries.
     *
     * @return language code ("vi", "en", etc.), never null
     */
    public static String getLanguage() {
        return getLocale().getLanguage();
    }

    /**
     * Clears the locale for this request thread.
     */
    public static void clear() {
        localeHolder.remove();
    }

    /**
     * Checks if a locale has been set for the current thread.
     *
     * @return true if locale is set, false otherwise
     */
    public static boolean isSet() {
        return localeHolder.get() != null;
    }
}
