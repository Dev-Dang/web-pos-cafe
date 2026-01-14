package com.laptrinhweb.zerostarcafe.core.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility for generating SEO-friendly URL slugs from Vietnamese text.
 * Converts "Cà Phê Sữa Đá" to "ca-phe-sua-da".
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * String slug = SlugUtil.toSlug("Cà Phê Sữa Đá");
 * // Returns: "ca-phe-sua-da"
 * 
 * String slug = SlugUtil.toSlug("Trà Sữa Trân Châu");
 * // Returns: "tra-sua-tran-chau"
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class SlugUtil {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");

    private SlugUtil() {
    }

    /**
     * Converts text to URL-friendly slug.
     * 
     * @param input the text to convert
     * @return the slug (lowercase, hyphenated)
     */
    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Convert Vietnamese characters
        String slug = convertVietnamese(input);
        
        // Normalize (decompose accents)
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        
        // Convert to lowercase
        slug = slug.toLowerCase(Locale.ENGLISH);
        
        // Replace whitespace with hyphens
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        
        // Remove non-alphanumeric characters (except hyphens)
        slug = NON_LATIN.matcher(slug).replaceAll("");
        
        // Remove multiple consecutive hyphens
        slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
        
        // Remove leading/trailing hyphens
        slug = slug.replaceAll("^-+|-+$", "");
        
        return slug;
    }

    /**
     * Converts Vietnamese characters to ASCII equivalents.
     * 
     * @param text the text to convert
     * @return the converted text
     */
    private static String convertVietnamese(String text) {
        return text
            // Lowercase vowels
            .replace("à", "a").replace("á", "a").replace("ạ", "a")
            .replace("ả", "a").replace("ã", "a")
            .replace("â", "a").replace("ầ", "a").replace("ấ", "a")
            .replace("ậ", "a").replace("ẩ", "a").replace("ẫ", "a")
            .replace("ă", "a").replace("ằ", "a").replace("ắ", "a")
            .replace("ặ", "a").replace("ẳ", "a").replace("ẵ", "a")
            .replace("è", "e").replace("é", "e").replace("ẹ", "e")
            .replace("ẻ", "e").replace("ẽ", "e")
            .replace("ê", "e").replace("ề", "e").replace("ế", "e")
            .replace("ệ", "e").replace("ể", "e").replace("ễ", "e")
            .replace("ì", "i").replace("í", "i").replace("ị", "i")
            .replace("ỉ", "i").replace("ĩ", "i")
            .replace("ò", "o").replace("ó", "o").replace("ọ", "o")
            .replace("ỏ", "o").replace("õ", "o")
            .replace("ô", "o").replace("ồ", "o").replace("ố", "o")
            .replace("ộ", "o").replace("ổ", "o").replace("ỗ", "o")
            .replace("ơ", "o").replace("ờ", "o").replace("ớ", "o")
            .replace("ợ", "o").replace("ở", "o").replace("ỡ", "o")
            .replace("ù", "u").replace("ú", "u").replace("ụ", "u")
            .replace("ủ", "u").replace("ũ", "u")
            .replace("ư", "u").replace("ừ", "u").replace("ứ", "u")
            .replace("ự", "u").replace("ử", "u").replace("ữ", "u")
            .replace("ỳ", "y").replace("ý", "y").replace("ỵ", "y")
            .replace("ỷ", "y").replace("ỹ", "y")
            .replace("đ", "d")
            // Uppercase vowels
            .replace("À", "A").replace("Á", "A").replace("Ạ", "A")
            .replace("Ả", "A").replace("Ã", "A")
            .replace("Â", "A").replace("Ầ", "A").replace("Ấ", "A")
            .replace("Ậ", "A").replace("Ẩ", "A").replace("Ẫ", "A")
            .replace("Ă", "A").replace("Ằ", "A").replace("Ắ", "A")
            .replace("Ặ", "A").replace("Ẳ", "A").replace("Ẵ", "A")
            .replace("È", "E").replace("É", "E").replace("Ẹ", "E")
            .replace("Ẻ", "E").replace("Ẽ", "E")
            .replace("Ê", "E").replace("Ề", "E").replace("Ế", "E")
            .replace("Ệ", "E").replace("Ể", "E").replace("Ễ", "E")
            .replace("Ì", "I").replace("Í", "I").replace("Ị", "I")
            .replace("Ỉ", "I").replace("Ĩ", "I")
            .replace("Ò", "O").replace("Ó", "O").replace("Ọ", "O")
            .replace("Ỏ", "O").replace("Õ", "O")
            .replace("Ô", "O").replace("Ồ", "O").replace("Ố", "O")
            .replace("Ộ", "O").replace("Ổ", "O").replace("Ỗ", "O")
            .replace("Ơ", "O").replace("Ờ", "O").replace("Ớ", "O")
            .replace("Ợ", "O").replace("Ở", "O").replace("Ỡ", "O")
            .replace("Ù", "U").replace("Ú", "U").replace("Ụ", "U")
            .replace("Ủ", "U").replace("Ũ", "U")
            .replace("Ư", "U").replace("Ừ", "U").replace("Ứ", "U")
            .replace("Ự", "U").replace("Ử", "U").replace("Ữ", "U")
            .replace("Ỳ", "Y").replace("Ý", "Y").replace("Ỵ", "Y")
            .replace("Ỷ", "Y").replace("Ỹ", "Y")
            .replace("Đ", "D");
    }

    /**
     * Generates a unique slug by appending a number if needed.
     * 
     * @param baseSlug the base slug
     * @param existingSlug the existing slug to avoid
     * @return unique slug
     */
    public static String makeUnique(String baseSlug, String existingSlug) {
        if (!baseSlug.equals(existingSlug)) {
            return baseSlug;
        }
        
        int counter = 1;
        String uniqueSlug;
        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (uniqueSlug.equals(existingSlug));
        
        return uniqueSlug;
    }
}
