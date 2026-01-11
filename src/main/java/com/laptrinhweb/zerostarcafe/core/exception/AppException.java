package com.laptrinhweb.zerostarcafe.core.exception;

/**
 * <h2>Description:</h2>
 * <p>
 * Base unchecked exception for application-level technical errors.
 * It is typically used to wrap lower-level exceptions such as
 * {@code SQLException}, {@code IOException}, etc.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}