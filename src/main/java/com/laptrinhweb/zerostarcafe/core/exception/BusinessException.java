package com.laptrinhweb.zerostarcafe.core.exception;

/**
 * <h2>Description:</h2>
 * <p>
 * Exception for business rule violations and domain logic errors.
 * These are recoverable errors that indicate the user's request
 * violates business rules but the system is functioning correctly.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}