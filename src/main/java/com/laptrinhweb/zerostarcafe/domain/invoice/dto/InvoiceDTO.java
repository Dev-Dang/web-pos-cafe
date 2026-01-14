package com.laptrinhweb.zerostarcafe.domain.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing invoice information for display.
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
public class InvoiceDTO {
    private long id;
    private long orderId;
    private String invoiceNumber;
    private long storeId;
    private String storeName;
    private Long userId;
    private LocalDateTime issuedAt;
    private String issuedAtDisplay;
    private int subtotalAmount;
    private int discountAmount;
    private int taxAmount;
    private int totalAmount;
    private String status;
    private String buyerName;
    private String buyerEmail;
    private LocalDateTime createdAt;
}
