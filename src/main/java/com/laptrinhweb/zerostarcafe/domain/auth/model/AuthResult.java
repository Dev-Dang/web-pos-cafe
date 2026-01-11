package com.laptrinhweb.zerostarcafe.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h2>Description:</h2>
 * <p>
 * Result model for authentication actions.
 * It stores whether the action was successful, a business {@code status}
 * (an enum), and optional {@code data} such as a {@link AuthUser}.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Registration result
 * AuthResult<AuthStatus, Void> r1 = AuthResult.ok(AuthStatus.REGISTER_SUCCESS);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 14/12/2025
 * @since 1.0.0
 */

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class AuthResult<S, T> {
    private final boolean success;
    private final S status;
    private final T data;

    // Creates a successful result with data.
    public static <S, T> AuthResult<S, T> ok(S status, T data) {
        return new AuthResult<>(true, status, data);
    }

    // Creates a successful result without data.
    public static <S, T> AuthResult<S, T> ok(S status) {
        return new AuthResult<>(true, status, null);
    }

    // Creates a failed result with optional data.
    public static <S, T> AuthResult<S, T> fail(S status, T data) {
        return new AuthResult<>(false, status, data);
    }

    // Creates a failed result without data.
    public static <S, T> AuthResult<S, T> fail(S status) {
        return new AuthResult<>(false, status, null);
    }
}
