package com.laptrinhweb.zerostarcafe.core.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * <h2>Description:</h2>
 * <p>
 * TokenUtil provides simple methods to generate, hash, and verify tokens.
 * The hashToken method can also be used for general-purpose SHA-256 hashing.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * String raw = TokenUtil.generateToken();
 * String hash = TokenUtil.hashToken(raw);
 * boolean ok  = TokenUtil.verifyToken(raw, hash);
 * 
 * // General SHA-256 hashing (reuse hashToken)
 * String itemHash = TokenUtil.hashToken("10|16,20|some note");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
public final class TokenUtil {

    // Secure random generator for cryptographic operations
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32; // 256-bit token

    // Prevent instantiation
    private TokenUtil() {
    }

    /**
     * Generates a cryptographically secure random token.
     * The result is Base64 URL-safe (no padding) and can be used
     * directly in cookies or headers.
     *
     * @return raw token string (to send to client)
     */
    public static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes); // fill with secure random bytes

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    /**
     * Hashes a string using SHA-256 and returns a hex string.
     * Can be used for tokens (stored in DB) or general-purpose hashing
     * (e.g., item hash for cart deduplication).
     *
     * @param input string to hash (if null, treated as empty string)
     * @return hex-encoded SHA-256 hash (64 characters)
     */
    public static String hashToken(String input) {
        if (input == null) {
            input = "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex string
            StringBuilder hex = new StringBuilder(hashed.length * 2);
            for (byte b : hashed) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * Verifies that the provided raw token matches the stored hash.
     *
     * @param token      raw token
     * @param storedHash stored hex hash from DB
     * @return {@code true} if token matches, otherwise {@code false}
     */
    public static boolean verifyToken(String token, String storedHash) {
        if (token == null || storedHash == null)
            return false;
        String hashed = hashToken(token);
        return constantTimeEquals(hashed, storedHash);
    }

    /**
     * Compares two strings in constant time to prevent timing attacks.
     * Made public for reuse across security components (CSRF, etc.).
     *
     * @param a the first string
     * @param b the second string
     * @return {@code true} if equal, {@code false} otherwise
     */
    public static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);

        if (aBytes.length != bBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }

        return result == 0;
    }
}