package com.laptrinhweb.zerostarcafe.domain.payment.model;

import lombok.Getter;

/**
 * <h2>Description:</h2>
 * <p>
 * Status codes for payment operations.
 * Each status has an associated i18n message key.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@Getter
public enum PaymentStatus {
    // Success statuses
    SUCCESS("general.payment.success"),

    // Validation failures
    CART_EMPTY("general.payment.cartEmpty"),
    CART_PRICE_CHANGED("general.payment.priceChanged"),
    CART_ITEMS_UNAVAILABLE("general.payment.itemsUnavailable"),
    INSUFFICIENT_POINTS("general.payment.insufficientPoints"),

    // System failures
    ORDER_CREATION_FAILED("general.payment.orderFailed"),
    PAYMENT_RECORD_FAILED("general.payment.recordFailed"),
    INVOICE_GENERATION_FAILED("general.payment.invoiceFailed"),
    LOYALTY_PROCESSING_FAILED("general.payment.loyaltyFailed"),
    SYSTEM_ERROR("general.payment.systemError");

    private final String messageKey;

    PaymentStatus(String messageKey) {
        this.messageKey = messageKey;
    }
}
