package com.laptrinhweb.zerostarcafe.domain.invoice.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for the invoice domain including statuses and format settings.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InvoiceConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Status {
        public static final String ISSUED = "issued";
        public static final String VOID = "void";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Format {
        public static final String INVOICE_NUMBER_PREFIX = "HD";  // Hóa đơn
        public static final String DATE_FORMAT = "yyyyMMdd";
        public static final int SEQUENCE_LENGTH = 5;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Tax {
        public static final double VAT_RATE = 0.08;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Attributes {
        public static final String INVOICE = "invoice";
        public static final String INVOICE_DETAIL = "invoiceDetail";
    }
}
