package com.laptrinhweb.zerostarcafe.core.i18n;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
import com.laptrinhweb.zerostarcafe.core.utils.JsonUtils;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class for extracting localized content from JSON strings.
 * Provides fallback logic and integrates with LocaleContext.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nUtils {

    /**
     * Extracts localized text from JSON using current request locale.
     *
     * @param jsonText the JSON containing localized content
     * @return localized text with fallback logic
     */
    public static String extract(String jsonText) {
        return extract(jsonText, LocaleContext.getLanguage());
    }

    /**
     * Extracts localized text from JSON for specific locale.
     *
     * @param jsonText the JSON containing localized content
     * @param language the target language code (e.g., "vi", "en")
     * @return localized text with fallback logic
     */
    public static String extract(String jsonText, String language) {
        if (jsonText == null || jsonText.trim().isEmpty()) {
            return "";
        }

        // If not valid JSON, return as-is
        if (!JsonUtils.isValidJson(jsonText)) {
            return jsonText;
        }

        // Try requested language
        String text = JsonUtils.getText(jsonText, language);
        if (text != null && !text.trim().isEmpty()) {
            return text;
        }

        // Fallback to default language
        String defaultText = JsonUtils.getText(jsonText, WebConstants.Locale.DEFAULT_LANGUAGE);
        if (defaultText != null && !defaultText.trim().isEmpty()) {
            return defaultText;
        }

        // Last resort: first available value
        return getFirstAvailableText(jsonText);
    }

    /**
     * Extracts localized text with custom fallback value.
     *
     * @param jsonText     the JSON containing localized content
     * @param language     the target language code
     * @param fallbackText the custom fallback if extraction fails
     * @return localized text, fallback, or empty string
     */
    public static String extractWithFallback(String jsonText, String language, String fallbackText) {
        String result = extract(jsonText, language);
        return (result == null || result.trim().isEmpty()) ?
                (fallbackText != null ? fallbackText : "") : result;
    }

    /**
     * Checks if JSON contains content for specific language.
     *
     * @param jsonText the JSON to check
     * @param language the language code to check
     * @return true if language exists with non-empty content
     */
    public static boolean hasLanguage(String jsonText, String language) {
        if (jsonText == null || language == null || !JsonUtils.isValidJson(jsonText)) {
            return false;
        }

        String content = JsonUtils.getText(jsonText, language);
        return content != null && !content.trim().isEmpty();
    }

    /**
     * Gets first available text from any language in JSON.
     */
    private static String getFirstAvailableText(String jsonText) {
        try {
            Map<String, Object> map = JsonUtils.toMap(jsonText);
            for (Object value : map.values()) {
                if (value instanceof String text && !text.trim().isEmpty()) {
                    return text;
                }
            }
        } catch (Exception e) {
            LoggerUtil.warn(I18nUtils.class,
                    "Cannot get first available text: " + e.getMessage());
        }
        return "";
    }
}