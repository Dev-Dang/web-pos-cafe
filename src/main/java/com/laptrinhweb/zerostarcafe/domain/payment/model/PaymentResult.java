package com.laptrinhweb.zerostarcafe.domain.payment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h2>Description:</h2>
 * <p>
 * Result model for payment operations.
 * Provides type-safe success/failure status with optional data.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Success with data
 * PaymentResult<PaymentStatus, PaymentResultDTO> result =
 *     PaymentResult.ok(PaymentStatus.SUCCESS, resultDTO);
 *
 * // Failure with validation error
 * PaymentResult<PaymentStatus, Void> result =
 *     PaymentResult.fail(PaymentStatus.CART_EMPTY);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentResult<S, T> {
    private final boolean success;
    private final S status;
    private final T data;

    /**
     * Creates a successful result with data.
     */
    public static <S, T> PaymentResult<S, T> ok(S status, T data) {
        return new PaymentResult<>(true, status, data);
    }

    /**
     * Creates a successful result without data.
     */
    public static <S, T> PaymentResult<S, T> ok(S status) {
        return new PaymentResult<>(true, status, null);
    }

    /**
     * Creates a failed result with data.
     */
    public static <S, T> PaymentResult<S, T> fail(S status, T data) {
        return new PaymentResult<>(false, status, data);
    }

    /**
     * Creates a failed result without data.
     */
    public static <S, T> PaymentResult<S, T> fail(S status) {
        return new PaymentResult<>(false, status, null);
    }
}
