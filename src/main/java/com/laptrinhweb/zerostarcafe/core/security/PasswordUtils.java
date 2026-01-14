package com.laptrinhweb.zerostarcafe.core.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * <h2>Description:</h2>
 * <p>
 * Secure password hashing & verification using <b>Argon2id</b>.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Convenience with String input (wipes a transient char[] buffer)
 * String hash = PasswordHashUtil.hash("MySecurePass@123");
 * boolean ok   = PasswordHashUtil.verify(hash, "MySecurePass@123");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class PasswordUtils {

    // Config follows OWASP guidance for web backends
    private static final int ITERATIONS = 3;
    private static final int MEMORY_KIB = 65_536;
    private static final int PARALLELISM = 1;

    // Prevent instantiation
    private PasswordUtils() {
    }

    // ===================== Preferred char[] API =====================

    /**
     * Hashes a password (char[]). Wipes the array before returning.
     *
     * @param password char[] password (will be wiped)
     * @return Argon2id hash (safe to store)
     */
    public static String hash(char[] password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        try {
            return argon2.hash(ITERATIONS, MEMORY_KIB, PARALLELISM, password);
        } finally {
            argon2.wipeArray(password);
        }
    }

    /**
     * Verifies a password (char[]) against an Argon2id hash. Wipes the array.
     *
     * @param hash     stored Argon2id hash
     * @param password char[] password (will be wiped)
     * @return true if password matches
     */
    public static boolean verify(String hash, char[] password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        try {
            return argon2.verify(hash, password);
        } finally {
            argon2.wipeArray(password);
        }
    }

    // ===================== Convenience String API =====================

    /**
     * Convenience wrapper for String input.
     * Converts to char[] internally and wipes the temporary buffer.
     *
     * @param plainPassword plaintext password (String cannot be wiped)
     * @return Argon2id hash
     */
    public static String hash(String plainPassword) {
        char[] buf = plainPassword.toCharArray();
        return hash(buf);
    }

    /**
     * Convenience wrapper for String input.
     * Converts to char[] internally and wipes the temporary buffer.
     *
     * @param plainPassword plaintext password (String cannot be wiped)
     * @param hash          stored Argon2id hash
     * @return true if password matches
     */
    public static boolean verify(String plainPassword, String hash) {
        char[] buf = plainPassword.toCharArray();
        return verify(hash, buf);
    }

}