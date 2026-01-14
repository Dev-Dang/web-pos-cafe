package com.laptrinhweb.zerostarcafe.domain.loyalty.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Enum representing the type of loyalty points transaction.
 * Maps to the {@code type} column in the {@code loyalty_transactions} table.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public enum TransactionType {
    EARN,
    REDEEM,
    EXPIRE,
    ADJUST
}
