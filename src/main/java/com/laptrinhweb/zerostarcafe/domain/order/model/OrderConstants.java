package com.laptrinhweb.zerostarcafe.domain.order.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for the order domain including status values and validation limits.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderConstants {

    /**
     * Order status values matching database enum.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Status {
        public static final String OPEN = "open";
        public static final String SERVED = "served";
        public static final String PARTIAL_PAID = "partial_paid";
        public static final String PAID = "paid";
        public static final String VOID = "void";
    }

    /**
     * Order source values matching database enum.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Source {
        public static final String QR = "qr";
        public static final String STAFF_POS = "staff_pos";
        public static final String KIOSK = "kiosk";
        public static final String WEB = "web";
    }
}
