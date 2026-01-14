package com.laptrinhweb.zerostarcafe.domain.order.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Enumeration of possible order statuses.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public enum OrderStatus {
    OPEN("open"),
    SERVED("served"),
    PARTIAL_PAID("partial_paid"),
    PAID("paid"),
    VOID("void");

    private final String dbValue;

    OrderStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static OrderStatus fromDbValue(String dbValue) {
        if (dbValue == null) {
            return OPEN;
        }
        for (OrderStatus status : values()) {
            if (status.dbValue.equalsIgnoreCase(dbValue)) {
                return status;
            }
        }
        return OPEN;
    }
}
