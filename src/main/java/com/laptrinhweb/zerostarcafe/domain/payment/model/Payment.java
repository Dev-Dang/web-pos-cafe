package com.laptrinhweb.zerostarcafe.domain.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a payment transaction.
 * Maps to the {@code payments} table.
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
public class Payment {
    private long id;
    private String sourceType;
    private Long orderId;
    private Long bookingId;
    private Long userId;
    private String method;
    private String type;
    private String status;
    private int amount;
    private int redeemPointsUsed;
    private int redeemValue;
    private LocalDateTime paidAt;
    private String txnRef;
}
