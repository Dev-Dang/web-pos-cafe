package com.laptrinhweb.zerostarcafe.domain.loyalty.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a loyalty points transaction record.
 * Maps directly to the {@code loyalty_transactions} table.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoyaltyTransaction {
    private long id;
    private long userId;
    private Long storeId;
    private TransactionType type;
    private int points;
    private Long paymentId;
    private LocalDate expiryDate;
    private String reason;
    private LocalDateTime occurredAt;
}
