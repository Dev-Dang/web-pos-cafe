package com.laptrinhweb.zerostarcafe.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing the result of a payment transaction.
 * Contains all relevant IDs and summary information after successful payment.
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResultDTO {
    private boolean success;
    private String message;
    private long paymentId;
    private String txnRef;
    private String paymentUrl;
    private String status;
    
    // NEW FIELDS for cash payment
    private long orderId;
    private long invoiceId;
    private String invoiceNumber;
    private int finalAmount;
    private int pointsEarned;
}
