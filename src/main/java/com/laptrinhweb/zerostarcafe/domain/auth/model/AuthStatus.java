package com.laptrinhweb.zerostarcafe.domain.auth.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Defines common result statuses for authentication actions
 * such as login and registration.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public enum AuthStatus {

    // Login / Register
    REGISTER_SUCCESS,
    REGISTER_FAILED,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    EMAIL_EXISTS,
    USERNAME_EXISTS,
    INVALID_CREDENTIALS,

    // Auth session state
    SESSION_NOT_FOUND,
    SESSION_REUSED,
    SESSION_ROTATED,
    SESSION_RESTORED,
    SESSION_INVALID
}