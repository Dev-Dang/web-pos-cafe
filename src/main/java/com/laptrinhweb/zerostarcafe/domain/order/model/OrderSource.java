package com.laptrinhweb.zerostarcafe.domain.order.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Enumeration of order sources indicating where the order originated.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public enum OrderSource {
    QR("qr"),
    STAFF_POS("staff_pos"),
    KIOSK("kiosk"),
    WEB("web");

    private final String dbValue;

    OrderSource(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static OrderSource fromDbValue(String dbValue) {
        if (dbValue == null) {
            return WEB;
        }
        for (OrderSource source : values()) {
            if (source.dbValue.equalsIgnoreCase(dbValue)) {
                return source;
            }
        }
        return WEB;
    }
}
