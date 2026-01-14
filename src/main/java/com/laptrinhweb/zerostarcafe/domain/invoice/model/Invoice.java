package com.laptrinhweb.zerostarcafe.domain.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an invoice for an order.
 * Maps to the {@code invoices} table.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invoice {
    private long id;
    private long orderId;
    private String invoiceNumber;
    private long storeId;
    private Long userId;
    private LocalDateTime issuedAt;
    private int subtotalAmount;
    private int discountAmount;
    private int taxAmount;
    private int totalAmount;
    private String status;
    private String buyerName;
    private String buyerTaxId;
    private String buyerAddress;
    private String buyerEmail;
    private LocalDateTime createdAt;
}
