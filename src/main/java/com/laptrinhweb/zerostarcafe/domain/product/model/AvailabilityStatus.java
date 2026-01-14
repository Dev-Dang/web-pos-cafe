package com.laptrinhweb.zerostarcafe.domain.product.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Enum for product availability status in store.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public enum AvailabilityStatus {
    AVAILABLE,
    SOLD_OUT,
    DISCONTINUED;

    public static AvailabilityStatus fromString(String value) {
        if (value == null) {
            return AVAILABLE;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AVAILABLE;
        }
    }

    public boolean isAvailable() {
        return this == AVAILABLE;
    }
}
