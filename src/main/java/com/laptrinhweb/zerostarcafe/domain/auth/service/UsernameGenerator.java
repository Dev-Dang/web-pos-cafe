package com.laptrinhweb.zerostarcafe.domain.auth.service;

import java.util.UUID;

/**
 * <h2>Description:</h2>
 * <p>
 * Generates unique usernames from email addresses.
 * Uses email local-part + random suffix to ensure uniqueness.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * - john.doe@example.com → john_doe_a8c2
 * - alice+tag@domain.com → alice_tag_f1e5
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class UsernameGenerator {

    private UsernameGenerator() {
    }

    /**
     * Generates a unique username from email address.
     *
     * @param email Email address
     * @return Generated username in format: local_part_suffix
     */
    public static String generate(String email) {
        if (email == null || email.isBlank()) {
            return generateRandomUsername();
        }

        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');

        if (atIndex <= 0) {
            return generateRandomUsername();
        }

        // Extract local-part (before @)
        String localPart = trimmed.substring(0, atIndex)
                .replace('.', '_')
                .replace('+', '_')
                .toLowerCase();

        // Generate 4-char random suffix
        String suffix = generateSuffix();

        return localPart + "_" + suffix;
    }

    /**
     * Generates a completely random username for edge cases.
     * Format: user_xxxxxxxx (8 random hex chars)
     *
     * @return Random username
     */
    private static String generateRandomUsername() {
        return "user_" + generateSuffix(8);
    }

    /**
     * Generates a random suffix from UUID.
     * Default 4 characters for normal usernames.
     *
     * @return Random hex string
     */
    private static String generateSuffix() {
        return generateSuffix(4);
    }

    /**
     * Generates a random hex suffix of specified length.
     *
     * @param length Number of characters
     * @return Random hex string
     */
    private static String generateSuffix(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, Math.min(length, uuid.length()));
    }
}
