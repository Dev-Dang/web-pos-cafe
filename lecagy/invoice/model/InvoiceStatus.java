package com.laptrinhweb.zerostarcafe.domain.invoice.model;

/**
 * Invoice status enumeration.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @since 1.0.0
 */
public enum InvoiceStatus {
    ISSUED("issued"),
    VOID("void");

    private final String dbValue;

    InvoiceStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static InvoiceStatus fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        for (InvoiceStatus status : values()) {
            if (status.dbValue.equals(dbValue)) {
                return status;
            }
        }
        return null;
    }
}