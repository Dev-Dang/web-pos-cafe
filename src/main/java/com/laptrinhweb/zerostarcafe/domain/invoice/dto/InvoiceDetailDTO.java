package com.laptrinhweb.zerostarcafe.domain.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing detailed invoice with order items breakdown.
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
public class InvoiceDetailDTO {
    private long id;
    private String invoiceNumber;
    private long orderId;
    private String storeName;
    private String storeAddress;
    private String customerName;
    private String customerPhone;
    private List<InvoiceItemDTO> items = new ArrayList<>();
    private int subtotal;
    private int taxAmount;
    private int discountAmount;
    private int totalAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime issuedAt;
    private String issuedTimeDisplay;
    private String issuedDateDisplay;
}
