package com.laptrinhweb.zerostarcafe.domain.payment.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for the payment domain including methods, statuses, and limits.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SourceType {
        public static final String ORDER = "order";
        public static final String BOOKING = "booking";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Method {
        public static final String CASH = "cash";
        public static final String CARD = "card";
        public static final String MOMO = "momo";
        public static final String VNPAY = "vnpay";
        public static final String ZALO = "zalo";
        public static final String WALLET = "wallet";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Type {
        public static final String DEPOSIT = "deposit";
        public static final String PREPAID = "prepaid";
        public static final String REMAINING = "remaining";
        public static final String REFUND = "refund";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Status {
        public static final String PENDING = "pending";
        public static final String PAID = "paid";
        public static final String FAILED = "failed";
        public static final String REFUNDED = "refunded";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Validation {
        public static final int MIN_AMOUNT = 1000;
        public static final int MAX_AMOUNT = 50000000;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Attributes {
        public static final String PAYMENT = "payment";
        public static final String PAYMENT_RESULT = "paymentResult";
    }
}
