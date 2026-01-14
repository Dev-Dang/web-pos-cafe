package com.laptrinhweb.zerostarcafe.core.exception;

/**
 * <h2>Description:</h2>
 * <p>
 * Exception for transaction management errors and database connection failures.
 * These are typically infrastructure-level errors that indicate programming
 * errors or system configuration issues that must be fixed immediately.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}